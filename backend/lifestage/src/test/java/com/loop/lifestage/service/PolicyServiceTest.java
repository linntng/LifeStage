package com.loop.lifestage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.loop.lifestage.dto.PolicyDTO;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.PolicyMapper;
import com.loop.lifestage.model.policy.Policy;
import com.loop.lifestage.model.policy.PolicyStatus;
import com.loop.lifestage.repository.PolicyRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

  @Mock private PolicyRepository policyRepository;

  @Mock private PolicyMapper policyMapper;

  @InjectMocks private PolicyService policyService;

  private Policy policy;
  private PolicyDTO policyDTO;

  @BeforeEach
  void setUp() {
    policy = createPolicy();
    policyDTO = createPolicyDTO();
  }

  // =========================
  // GET ALL POLICIES
  // =========================

  @Test
  void getAllPolicies_shouldReturnMappedPolicies() {

    // Given
    when(policyRepository.findAll()).thenReturn(List.of(policy));
    when(policyMapper.toPolicyDTO(policy)).thenReturn(policyDTO);

    // When
    List<PolicyDTO> result = policyService.getAllPolicies();

    // Then
    assertEquals(1, result.size());
    assertEquals(policyDTO.getId(), result.get(0).getId());
    assertEquals(policyDTO.getName(), result.get(0).getName());
    assertEquals(policyDTO.getPremium(), result.get(0).getPremium());
    assertEquals(policyDTO.getStatus(), result.get(0).getStatus());

    verify(policyRepository).findAll();
    verify(policyMapper).toPolicyDTO(policy);
  }

  @Test
  void getAllPolicies_shouldThrowResourceNotFound_whenEntityNotFoundExceptionOccurs() {

    // Given
    when(policyRepository.findAll()).thenThrow(new EntityNotFoundException());

    // Then
    assertThrows(ResourceNotFoundException.class, () -> policyService.getAllPolicies());
  }

  @Test
  void getAllPolicies_shouldThrowRuntimeException_whenUnexpectedExceptionOccurs() {

    // Given
    when(policyRepository.findAll()).thenThrow(new RuntimeException("database error"));

    // Then
    assertThrows(RuntimeException.class, () -> policyService.getAllPolicies());
  }

  // =========================
  // TEST DATA FACTORIES
  // =========================

  private Policy createPolicy() {
    Policy policy = new Policy();
    policy.setId(1L);
    policy.setName("Life Coverage");
    policy.setPremium(199.99f);
    policy.setStatus(PolicyStatus.ACTIVE);
    policy.setCoveredLifeEvents(Collections.emptySet());
    policy.setCoveredUsers(Collections.emptySet());
    return policy;
  }

  private PolicyDTO createPolicyDTO() {
    PolicyDTO dto = new PolicyDTO();
    dto.setId(1L);
    dto.setName("Life Coverage");
    dto.setPremium(199.99f);
    dto.setStatus(PolicyStatus.ACTIVE);
    dto.setCoveredLifeEvents(Collections.emptySet());
    return dto;
  }
}
