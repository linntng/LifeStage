package com.loop.lifestage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.loop.lifestage.dto.PolicyDTO;
import com.loop.lifestage.mapper.PolicyMapper;
import com.loop.lifestage.mapper.PolicyRejectionMapper;
import com.loop.lifestage.model.policy.*;
import com.loop.lifestage.model.user.User;
import com.loop.lifestage.model.user.UserRole;
import com.loop.lifestage.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
  @Mock private UserRepository userRepository;
  @Mock private PolicyManagerActionRepository policyManagerActionRepository;
  @Mock private PolicyRejectionRepository policyRejectionRepository;
  @Mock private PolicyRejectionMapper policyRejectionMapper;

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
    when(policyRepository.findAll()).thenReturn(List.of(policy));
    when(policyMapper.toPolicyDTO(policy)).thenReturn(policyDTO);

    List<PolicyDTO> result = policyService.getAllPolicies();

    assertEquals(1, result.size());
    verify(policyRepository).findAll();
    verify(policyMapper).toPolicyDTO(policy);
  }

  @Test
  void getAllPolicies_shouldThrowRuntimeException_whenUnexpectedExceptionOccurs() {
    when(policyRepository.findAll()).thenThrow(new RuntimeException());

    assertThrows(RuntimeException.class, () -> policyService.getAllPolicies());
  }

  // =========================
  // CREATE POLICY
  // =========================

  @Test
  void createPolicy_shouldCreateDraftPolicy_whenManagerIsPolicyManager() {

    User manager = new User();
    manager.setId("1");
    manager.setRole(UserRole.POLICY_MANAGER);

    when(userRepository.findById("1")).thenReturn(Optional.of(manager));
    when(policyMapper.toPolicy(policyDTO)).thenReturn(policy);
    when(policyRepository.save(policy)).thenReturn(policy);
    when(policyMapper.toPolicyDTO(policy)).thenReturn(policyDTO);

    PolicyDTO result = policyService.createPolicy("1", policyDTO);

    assertNotNull(result);
    assertEquals(PolicyStatus.DRAFT, policy.getStatus());
    assertTrue(policy.getInReview());

    verify(policyRepository).save(policy);
    verify(policyManagerActionRepository).save(any());
  }

  @Test
  void createPolicy_shouldThrow_whenUserNotFound() {
    when(userRepository.findById("1")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
        () -> policyService.createPolicy("1", policyDTO));
  }

  @Test
  void createPolicy_shouldThrow_whenUserNotPolicyManager() {
    User user = new User();
    user.setRole(UserRole.USER);

    when(userRepository.findById("1")).thenReturn(Optional.of(user));

    assertThrows(RuntimeException.class,
        () -> policyService.createPolicy("1", policyDTO));
  }

  // =========================
  // UPDATE POLICY - SUCCESS PATHS
  // =========================

  @Test
  void updatePolicy_shouldCreatePendingVersion_whenStatusIsActive() {

    User manager = new User();
    manager.setId("1");
    manager.setRole(UserRole.POLICY_MANAGER);

    Policy activePolicy = createPolicy();
    PolicyManagerAction action =
        new PolicyManagerAction(manager, null, activePolicy);

    Policy mappedPolicy = createPolicy();

    when(userRepository.findById("1")).thenReturn(Optional.of(manager));
    when(policyRepository.findById(policyDTO.getId())).thenReturn(Optional.of(activePolicy));
    when(policyManagerActionRepository
        .findTopBySuggestedPolicyIdOrderByCreatedAtDesc(activePolicy.getId()))
        .thenReturn(Optional.of(action));

    when(policyMapper.toPolicy(policyDTO)).thenReturn(mappedPolicy);
    when(policyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    when(policyMapper.toPolicyDTO(any())).thenReturn(policyDTO);

    PolicyDTO result = policyService.updatePolicy("1", policyDTO, null);

    assertNotNull(result);
    assertEquals(PolicyStatus.PENDING, mappedPolicy.getStatus());
    assertNull(mappedPolicy.getId());

    verify(policyManagerActionRepository).save(any());
  }

  @Test
  void updatePolicy_shouldHandleDraftPolicy() {

    User manager = new User();
    manager.setId("1");
    manager.setRole(UserRole.POLICY_MANAGER);

    Policy draft = createPolicy();
    draft.setStatus(PolicyStatus.DRAFT);

    PolicyManagerAction action =
        new PolicyManagerAction(manager, null, draft);

    when(userRepository.findById("1")).thenReturn(Optional.of(manager));
    when(policyRepository.findById(policyDTO.getId())).thenReturn(Optional.of(draft));
    when(policyManagerActionRepository
        .findTopBySuggestedPolicyIdOrderByCreatedAtDesc(draft.getId()))
        .thenReturn(Optional.of(action));

    when(policyMapper.toPolicy(policyDTO)).thenReturn(draft);
    when(policyRepository.save(any())).thenReturn(draft);
    when(policyMapper.toPolicyDTO(any())).thenReturn(policyDTO);

    PolicyDTO result = policyService.updatePolicy("1", policyDTO, null);

    assertNotNull(result);
    assertFalse(draft.getInReview());

    verify(policyRepository).save(draft);
  }

  @Test
  void updatePolicy_shouldHandlePendingPolicy() {

    User manager = new User();
    manager.setId("1");
    manager.setRole(UserRole.POLICY_MANAGER);

    Policy origin = createPolicy();
    origin.setStatus(PolicyStatus.ACTIVE);

    Policy suggested = createPolicy();
    suggested.setStatus(PolicyStatus.PENDING);

    PolicyManagerAction action =
        new PolicyManagerAction(manager, origin, suggested);

    when(userRepository.findById("1")).thenReturn(Optional.of(manager));
    when(policyRepository.findById(policyDTO.getId())).thenReturn(Optional.of(suggested));
    when(policyManagerActionRepository
        .findTopBySuggestedPolicyIdOrderByCreatedAtDesc(suggested.getId()))
        .thenReturn(Optional.of(action));

    when(policyMapper.toPolicy(policyDTO)).thenReturn(suggested);
    when(policyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    when(policyMapper.toPolicyDTO(any())).thenReturn(policyDTO);

    PolicyDTO result = policyService.updatePolicy("1", policyDTO, null);

    assertNotNull(result);
    verify(policyRepository).save(origin);
    verify(policyRepository).save(suggested);
    verify(policyManagerActionRepository).save(any());
  }

  // =========================
  // UPDATE POLICY - FAILURE PATHS
  // =========================

  @Test
  void updatePolicy_shouldThrow_whenManagerNotFound() {
    when(userRepository.findById("1")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
        () -> policyService.updatePolicy("1", policyDTO, null));
  }

  @Test
  void updatePolicy_shouldThrow_whenUserNotPolicyManager() {

    User user = new User();
    user.setRole(UserRole.USER);

    when(userRepository.findById("1")).thenReturn(Optional.of(user));

    assertThrows(RuntimeException.class,
        () -> policyService.updatePolicy("1", policyDTO, null));
  }

  @Test
  void updatePolicy_shouldThrow_whenPolicyNotFound() {

    User manager = new User();
    manager.setRole(UserRole.POLICY_MANAGER);

    when(userRepository.findById("1")).thenReturn(Optional.of(manager));
    when(policyRepository.findById(policyDTO.getId())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
        () -> policyService.updatePolicy("1", policyDTO, null));
  }

  @Test
  void updatePolicy_shouldThrow_whenLatestActionNotFound() {

    User manager = new User();
    manager.setRole(UserRole.POLICY_MANAGER);

    when(userRepository.findById("1")).thenReturn(Optional.of(manager));
    when(policyRepository.findById(policyDTO.getId())).thenReturn(Optional.of(policy));
    when(policyManagerActionRepository
        .findTopBySuggestedPolicyIdOrderByCreatedAtDesc(policy.getId()))
        .thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
        () -> policyService.updatePolicy("1", policyDTO, null));
  }

  @Test
  void updatePolicy_shouldThrow_whenUnsupportedStatus() {

    User manager = new User();
    manager.setRole(UserRole.POLICY_MANAGER);

    policy.setStatus(PolicyStatus.EXPIRED);

    PolicyManagerAction action =
        new PolicyManagerAction(manager, null, policy);

    when(userRepository.findById("1")).thenReturn(Optional.of(manager));
    when(policyRepository.findById(policyDTO.getId())).thenReturn(Optional.of(policy));
    when(policyManagerActionRepository
        .findTopBySuggestedPolicyIdOrderByCreatedAtDesc(policy.getId()))
        .thenReturn(Optional.of(action));

    assertThrows(RuntimeException.class,
        () -> policyService.updatePolicy("1", policyDTO, null));
  }

  // =========================
  // TEST DATA
  // =========================

  private Policy createPolicy() {
    Policy p = new Policy();
    p.setId(1L);
    p.setName("Life Coverage");
    p.setPremium(199.99f);
    p.setStatus(PolicyStatus.ACTIVE);
    p.setCoveredLifeEvents(Collections.emptySet());
    p.setCoveredUsers(Collections.emptySet());
    return p;
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