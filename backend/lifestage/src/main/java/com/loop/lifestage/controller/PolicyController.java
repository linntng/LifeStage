package com.loop.lifestage.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loop.lifestage.dto.PolicyDTO;
import com.loop.lifestage.service.AuditService;
import com.loop.lifestage.service.PolicyService;

@RestController
@RequestMapping("/policies")
public class PolicyController {

  private final PolicyService policyService;
  private final AuditService auditService;

  public PolicyController(PolicyService policyService, AuditService auditService) {
    this.policyService = policyService;
    this.auditService = auditService;
  }

  @GetMapping("")
  public ResponseEntity<List<PolicyDTO>> getAllPolicies() {
    auditService.logAccess(
      "READ_LIST", 
      "Policies"
    );
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
  
}
