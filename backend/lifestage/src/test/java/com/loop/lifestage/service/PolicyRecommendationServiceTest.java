package com.loop.lifestage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.loop.lifestage.dto.PolicyRecommendationDTO;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.PolicyRecommendationMapper;
import com.loop.lifestage.model.policy.PolicyRecommendation;
import com.loop.lifestage.repository.PolicyRecommendationRepository;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PolicyRecommendationServiceTest {

  @Mock
  private PolicyRecommendationRepository policyRecommendationRepository;

  @Mock
  private PolicyRecommendationMapper policyRecommendationMapper;

  @InjectMocks
  private PolicyRecommendationService policyRecommendationService;

  private PolicyRecommendation policyRecommendation;
  private PolicyRecommendationDTO policyRecommendationDTO;

  @BeforeEach
  void setUp() {
    policyRecommendation = createPolicyRecommendation();
    policyRecommendationDTO = createPolicyRecommendationDTO();
  }

  // =========================
  // CREATE POLICY RECOMMENDATION
  // =========================

  @Test
  void createPolicyRecommendation_shouldSaveAndReturnMappedDTO() {

    // Given
    when(policyRecommendationMapper.toEntity(policyRecommendationDTO))
        .thenReturn(policyRecommendation);

    when(policyRecommendationRepository.saveAndFlush(policyRecommendation))
        .thenReturn(policyRecommendation);

    when(policyRecommendationMapper.toDto(policyRecommendation))
        .thenReturn(policyRecommendationDTO);

    // When
    PolicyRecommendationDTO result =
        policyRecommendationService.createPolicyRecommendation(policyRecommendationDTO);

    // Then
    assertNotNull(result);
    assertEquals(policyRecommendationDTO.getUserId(), result.getUserId());

    verify(policyRecommendationMapper).toEntity(policyRecommendationDTO);
    verify(policyRecommendationRepository).saveAndFlush(policyRecommendation);
    verify(policyRecommendationMapper).toDto(policyRecommendation);
  }

  @Test
  void createPolicyRecommendation_shouldThrowRuntimeException_whenUnexpectedExceptionOccurs() {

    // Given
    when(policyRecommendationMapper.toEntity(policyRecommendationDTO))
        .thenThrow(new RuntimeException("mapping error"));

    // Then
    assertThrows(
        RuntimeException.class,
        () -> policyRecommendationService.createPolicyRecommendation(policyRecommendationDTO));
  }

  // =========================
  // TEST DATA FACTORIES
  // =========================

  private PolicyRecommendation createPolicyRecommendation() {
    return new PolicyRecommendation();
  }

  private PolicyRecommendationDTO createPolicyRecommendationDTO() {
    PolicyRecommendationDTO dto = new PolicyRecommendationDTO();
    dto.setUserId("user123");
    dto.setPremiumImpact(100f);
    dto.setLifeEventId(1L);
    return dto;
  }
}