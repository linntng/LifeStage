package com.loop.lifestage.service;

import com.loop.lifestage.dto.PolicyRecommendationDTO;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.PolicyRecommendationMapper;
import com.loop.lifestage.model.policy.PolicyRecommendation;
import com.loop.lifestage.repository.PolicyRecommendationRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional(readOnly = true)
  public PolicyRecommendationDTO getLatestPolicyRecommendationForUser(String userId) {
    try {

      PolicyRecommendation recommendation =
          policyRecommendationRepository
              .findTopByUserIdOrderByCreatedAtDesc(userId)
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "No policy recommendations found for user id: " + userId));

      return policyRecommendationMapper.toDto(recommendation);

    } catch (ResourceNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(
          "An error occurred while fetching the latest policy recommendation", e);
    }
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