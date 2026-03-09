package com.loop.lifestage.controller;

import com.loop.lifestage.dto.PolicyDTO;
import com.loop.lifestage.service.PolicyService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
