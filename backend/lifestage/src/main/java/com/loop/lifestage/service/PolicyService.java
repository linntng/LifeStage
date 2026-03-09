package com.loop.lifestage.service;

import com.loop.lifestage.dto.PolicyDTO;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.PolicyMapper;
import com.loop.lifestage.model.policy.Policy;
import com.loop.lifestage.repository.PolicyRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolicyService {

  private final PolicyRepository policyRepository;
  private final PolicyMapper policyMapper;

  public PolicyService(PolicyRepository policyRepository, PolicyMapper policyMapper) {
    this.policyRepository = policyRepository;
    this.policyMapper = policyMapper;
  }

  @Transactional(readOnly = true)
  public List<PolicyDTO> getAllPolicies() {
    try {
      List<Policy> policies = policyRepository.findAll();
      return policies.stream()
          .map(policyMapper::toPolicyDTO)
          .collect(Collectors.toList());
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Policies not found");
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while fetching policies", e);
    }
  }
}