package com.loop.lifestage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loop.lifestage.dto.PolicyRecommendationDTO;
import com.loop.lifestage.mapper.PolicyRecommendationMapper;
import com.loop.lifestage.model.policy.PolicyRecommendation;
import com.loop.lifestage.repository.PolicyRecommendationRepository;

@Service
public class PolicyRecommendationService {

  private final PolicyRecommendationRepository policyRecommendationRepository;
  private final PolicyRecommendationMapper policyRecommendationMapper;

  public PolicyRecommendationService(
      PolicyRecommendationRepository policyRecommendationRepository,
      PolicyRecommendationMapper policyRecommendationMapper) {
    this.policyRecommendationRepository = policyRecommendationRepository;
    this.policyRecommendationMapper = policyRecommendationMapper;
  }

  @Transactional
  public PolicyRecommendationDTO createPolicyRecommendation(
      PolicyRecommendationDTO recommendationDTO) {
    try {
      PolicyRecommendation recommendation =
          policyRecommendationMapper.toEntity(recommendationDTO);

      return policyRecommendationMapper.toDto(
          policyRecommendationRepository.saveAndFlush(recommendation));

    } catch (Exception e) {
      throw new RuntimeException(
          "Something went wrong when trying to create policy recommendation", e);
    }
  }
}