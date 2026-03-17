package com.loop.lifestage.service;

import com.loop.lifestage.dto.PolicyDTO;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.PolicyMapper;
import com.loop.lifestage.model.policy.Policy;
import com.loop.lifestage.model.policy.PolicyStatus;
import com.loop.lifestage.model.user.User;
import com.loop.lifestage.model.user.UserRole;
import com.loop.lifestage.repository.PolicyRepository;
import com.loop.lifestage.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolicyService {

  private final PolicyRepository policyRepository;
  private final PolicyMapper policyMapper;
  private final UserRepository userRepository;

  public PolicyService(PolicyRepository policyRepository, PolicyMapper policyMapper, UserRepository userRepository) {
    this.policyRepository = policyRepository;
    this.policyMapper = policyMapper;
    this.userRepository = userRepository;
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
        policy.setStatus(PolicyStatus.ACTIVE);
        return policyMapper.toPolicyDTO(policyRepository.save(policy));
      } else {
        throw new RuntimeException("Could not create new policy");
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Transactional
  public PolicyDTO updatePolicy(String managerId, PolicyDTO policyDTO) {
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
        Policy newPolicy = policyMapper.toPolicy(policyDTO);
        oldPolicy.setStatus(PolicyStatus.EXPIRED);
        newPolicy.setId(null);
        newPolicy.setStatus(PolicyStatus.ACTIVE);
        policyRepository.save(oldPolicy);
        return policyMapper.toPolicyDTO((policyRepository.save(newPolicy)));
      } else {
        throw new RuntimeException("Could not create new policy");
      }
    } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
    }
  }
}
