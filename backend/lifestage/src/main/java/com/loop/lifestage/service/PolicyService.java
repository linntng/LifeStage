package com.loop.lifestage.service;

import com.loop.lifestage.dto.PolicyDTO;
import com.loop.lifestage.dto.PolicyRejectionDTO;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.PolicyMapper;
import com.loop.lifestage.mapper.PolicyRejectionMapper;
import com.loop.lifestage.model.policy.Policy;
import com.loop.lifestage.model.policy.PolicyManagerAction;
import com.loop.lifestage.model.policy.PolicyRejection;
import com.loop.lifestage.model.policy.PolicyStatus;
import com.loop.lifestage.model.user.User;
import com.loop.lifestage.model.user.UserRole;
import com.loop.lifestage.repository.PolicyManagerActionRepository;
import com.loop.lifestage.repository.PolicyRejectionRepository;
import com.loop.lifestage.repository.PolicyRepository;
import com.loop.lifestage.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolicyService {

  private final PolicyRepository policyRepository;
  private final PolicyMapper policyMapper;
  private final UserRepository userRepository;
  private final PolicyManagerActionRepository policyManagerActionRepository;
  private final PolicyRejectionRepository policyRejectionRepository;
  private final PolicyRejectionMapper policyRejectionMapper;

  public PolicyService(PolicyRepository policyRepository, 
    PolicyMapper policyMapper, 
    UserRepository userRepository, 
    PolicyManagerActionRepository policyManagerActionRepository,
    PolicyRejectionRepository policyRejectionRepository,
    PolicyRejectionMapper policyRejectionMapper) {
    this.policyRepository = policyRepository;
    this.policyMapper = policyMapper;
    this.userRepository = userRepository;
    this.policyManagerActionRepository = policyManagerActionRepository;
    this.policyRejectionRepository = policyRejectionRepository;
    this.policyRejectionMapper = policyRejectionMapper;
  }

  @Transactional(readOnly = true)
  public List<PolicyDTO> getAllPolicies() {
    try {
      List<Policy> policies = policyRepository.findAll();
      return policies.stream().map(policyMapper::toPolicyDTO).collect(Collectors.toList());
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Policies not found");
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while fetching policies", e);
    }
  }

  @Transactional
  public PolicyDTO createPolicy(String managerId, PolicyDTO policyDTO) {
    try {
      User manager = 
        userRepository
              .findById(managerId)
              .orElseThrow(
                  () -> new EntityNotFoundException("User not found with id: " + managerId));
      if (manager.getRole() == UserRole.POLICY_MANAGER) {
        Policy policy = policyMapper.toPolicy(policyDTO);
        policy.setStatus(PolicyStatus.DRAFT);
        policy.setInReview(true);
        policyRepository.save(policy);
        PolicyManagerAction managerAction = new PolicyManagerAction(manager, null, policy);
        policyManagerActionRepository.save(managerAction);
        return policyMapper.toPolicyDTO(policy);
      } else {
        throw new RuntimeException("Could not create new policy");
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Transactional
  public PolicyDTO updatePolicy(String managerId, PolicyDTO policyDTO, PolicyRejectionDTO policyRejectionDTO) {
      try {
      User manager = 
        userRepository
              .findById(managerId)
              .orElseThrow(
                  () -> new EntityNotFoundException("User not found with id: " + managerId));
      if (manager.getRole() == UserRole.POLICY_MANAGER) {
        Policy oldPolicy = policyRepository.findById(policyDTO.getId())
                              .orElseThrow(
                                () -> new EntityNotFoundException("Policy not found with id: " + policyDTO.getId()));
        PolicyManagerAction latestManagerAction = policyManagerActionRepository.findTopBySuggestedPolicyIdOrderByCreatedAtDesc(oldPolicy.getId())
                                                    .orElseThrow(
                                                      () -> new EntityNotFoundException("Latest action not found"));
        PolicyStatus latestStatus = latestManagerAction.getSuggestedPolicy().getStatus();
        if (latestStatus == PolicyStatus.DRAFT) {
          return handleDraftPolicyUpdate(manager, latestManagerAction, policyDTO, policyRejectionDTO);
        } else if (latestStatus == PolicyStatus.PENDING) {
          return handlePendingPolicyUpdate(manager, latestManagerAction, policyDTO, policyRejectionDTO);
        } else if (latestStatus == PolicyStatus.ACTIVE) {
          return handleActivePolicyUpdate(manager, latestManagerAction, policyDTO);
        } else {
          throw new RuntimeException("Something went wrong when trying to handle policy update");
        }    
      } else {
        throw new RuntimeException("Could not create update policy");
      }
    } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Transactional(readOnly = true)
  public Set<PolicyDTO> getPoliciesToReviewForPolicyManager(String managerId) {
    try {
      User manager = userRepository.findById(managerId)
        .orElseThrow(() -> new RuntimeException("Manager not found"));
      if (manager.getRole() == UserRole.POLICY_MANAGER) {
          Set<Long> policyIds =
            policyManagerActionRepository.findDistinctSuggestedPolicyIdsByManagerIdNot(managerId);

      return policyRepository.findAllById(policyIds).stream()
          .filter(policy -> policy.getStatus() == PolicyStatus.DRAFT
                        || policy.getStatus() == PolicyStatus.PENDING)
          .map(policyMapper::toPolicyDTO)
          .collect(Collectors.toSet());
      } else {
        throw new RuntimeException("Something went wrong when trying to fetch policies");
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Transactional(readOnly = true)
  public Set<PolicyRejectionDTO> getRejectedPoliciesForManager(String managerId) {
    try {
      return policyRejectionRepository.findByAuthorId(managerId)
        .stream()
        .map(policyRejectionMapper::toDto)
        .collect(Collectors.toSet());
    } catch (Exception e) {
      throw new RuntimeException("Something went wrong when trying to fetch rejected policies for manager: " + managerId);
    }
  }

  private PolicyDTO handleDraftPolicyUpdate(User manager, PolicyManagerAction latestManagerAction, PolicyDTO policyDTO, PolicyRejectionDTO policyRejectionDTO) {
    Policy policy = policyMapper.toPolicy(policyDTO);
    policy.setInReview(false);
    policyRepository.save(policy);
    if (policy.getStatus() == PolicyStatus.CANCELLED) {
      User author = latestManagerAction.getManager();
      policyRejectionRepository.save(new PolicyRejection(author, manager, policy, policyRejectionDTO.getExplanation()));
    }
    PolicyManagerAction newManagerAction = new PolicyManagerAction(manager, null, policy);
    policyManagerActionRepository.save(newManagerAction);
    return policyMapper.toPolicyDTO(policy);
  }

  private PolicyDTO handlePendingPolicyUpdate(User manager, PolicyManagerAction latestManagerAction, PolicyDTO policyDTO, PolicyRejectionDTO policyRejectionDTO) {
    Policy suggestedPolicy = policyMapper.toPolicy(policyDTO);
    Policy originPolicy = latestManagerAction.getOriginPolicy();
    if (suggestedPolicy.getStatus() == PolicyStatus.ACTIVE) {
      originPolicy.setStatus(PolicyStatus.EXPIRED);
    } else if (suggestedPolicy.getStatus() == PolicyStatus.CANCELLED) {
      originPolicy.setStatus(PolicyStatus.ACTIVE);
      User author = latestManagerAction.getManager();
      policyRejectionRepository.save(new PolicyRejection(author, manager, suggestedPolicy, policyRejectionDTO.getExplanation()));
    }
    originPolicy.setInReview(false);
    policyRepository.save(originPolicy);
    policyRepository.save(suggestedPolicy);
    PolicyManagerAction newManagerAction = new PolicyManagerAction(manager, originPolicy, suggestedPolicy);
    policyManagerActionRepository.save(newManagerAction);
    return policyMapper.toPolicyDTO(suggestedPolicy);
  }

    private PolicyDTO handleActivePolicyUpdate(User manager, PolicyManagerAction managerAction, PolicyDTO policyDTO) {
      Policy suggestedPolicy = policyMapper.toPolicy(policyDTO);
      suggestedPolicy.setId(null);
      suggestedPolicy.setStatus((PolicyStatus.PENDING));
      Policy originPolicy = managerAction.getSuggestedPolicy();
      originPolicy.setInReview(true);
      policyRepository.save(originPolicy);
      PolicyDTO suggestedPolicyDTO = policyMapper.toPolicyDTO(policyRepository.save(suggestedPolicy));
      PolicyManagerAction newManagerAction = new PolicyManagerAction(manager, originPolicy, suggestedPolicy);
      policyManagerActionRepository.save(newManagerAction);
      return suggestedPolicyDTO;
  }
}
