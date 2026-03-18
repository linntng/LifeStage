package com.loop.lifestage.controller;

import com.loop.lifestage.dto.PolicyDTO;
import com.loop.lifestage.service.PolicyService;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/policies")
public class PolicyController {

  private final PolicyService policyService;

  public PolicyController(PolicyService policyService) {
    this.policyService = policyService;
  }

  @GetMapping("")
  public ResponseEntity<List<PolicyDTO>> getAllPolicies() {
    return ResponseEntity.ok(policyService.getAllPolicies());
  }

  @PostMapping("")
  public ResponseEntity<PolicyDTO> postPolicy(
    @RequestBody PolicyDTO policyDTO,
    @AuthenticationPrincipal Jwt jwt) {
      String sub = jwt.getSubject();
      return ResponseEntity.ok(policyService.createPolicy(sub, policyDTO));
  }

  @PatchMapping("")
  public ResponseEntity<PolicyDTO> updatePolicy(
    @RequestBody PolicyDTO policyDTO,
    @AuthenticationPrincipal Jwt jwt) {
      String sub = jwt.getSubject();
      return ResponseEntity.ok(policyService.updatePolicy(sub, policyDTO));
    }

  @GetMapping("/review")
  public ResponseEntity<Set<PolicyDTO>> getReviewPoliciesForManager(@AuthenticationPrincipal Jwt jwt) {
    String sub = jwt.getSubject();
    return ResponseEntity.ok(policyService.getPoliciesToReviewForPolicyManager(sub));
  }
}
