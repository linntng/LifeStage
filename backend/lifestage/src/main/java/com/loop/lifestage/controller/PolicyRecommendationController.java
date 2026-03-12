package com.loop.lifestage.controller;

import com.loop.lifestage.dto.PolicyRecommendationDTO;
import com.loop.lifestage.service.PolicyRecommendationService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/policyrecommendations")
public class PolicyRecommendationController {

  private final PolicyRecommendationService policyRecommendationService;

  public PolicyRecommendationController(
      PolicyRecommendationService policyRecommendationService) {
    this.policyRecommendationService = policyRecommendationService;
  }

  @GetMapping("/{userId}")
  @PreAuthorize("#userId == authentication.token.claims['sub']")
  public ResponseEntity<PolicyRecommendationDTO> getLatestPolicyRecommendationForUser(
      @PathVariable String userId) {

    return ResponseEntity.ok(
        policyRecommendationService.getLatestPolicyRecommendationForUser(userId));
  }

  @PostMapping("/{userId}")
  @PreAuthorize("#userId == authentication.token.claims['sub']")
  public ResponseEntity<PolicyRecommendationDTO> createPolicyRecommendationForUser(
      @PathVariable String userId,
      @RequestBody PolicyRecommendationDTO policyRecommendationDTO) {

    return ResponseEntity.ok(
        policyRecommendationService.createPolicyRecommendation(policyRecommendationDTO));
  }
}