package com.loop.lifestage.controller;

import java.util.List;
import com.loop.lifestage.dto.PatchPolicyRequest;
import com.loop.lifestage.dto.PolicyAuditDTO;
import com.loop.lifestage.dto.PolicyDTO;
import com.loop.lifestage.dto.PolicyRejectionDTO;
import com.loop.lifestage.model.policy.Policy;
import com.loop.lifestage.service.PolicyService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Set;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.query.AuditEntity;

import org.springframework.web.bind.annotation.*;
import com.loop.lifestage.mapper.PolicyMapper;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/policies")
public class PolicyController {

  private final PolicyService policyService;
  private final AuditService auditService;
  private final PolicyMapper policyMapper;
  @PersistenceContext
  private EntityManager entityManager;


  public PolicyController(PolicyService policyService, AuditService auditService, PolicyMapper policyMapper) {
    this.policyService = policyService;
    this.auditService = auditService;
    this.policyMapper = policyMapper;
  }

@GetMapping("/audit")
public List<PolicyAuditDTO> getAudit() {
    AuditReader reader = AuditReaderFactory.get(entityManager);

    List<Object[]> results = reader.createQuery()
        .forRevisionsOfEntity(Policy.class, false, true)
        .addOrder(AuditEntity.revisionProperty("timestamp").asc())
        .getResultList();

    return results.stream()
        .map((Object[] r) -> {
            Policy policy = (Policy) r[0];

            // 🔁 map entity -> DTO
            PolicyDTO policyDTO = policyMapper.toPolicyDTO(policy);

            return new PolicyAuditDTO(
                policyDTO,
                ((RevisionType) r[2]).name(),
                Instant.ofEpochMilli(((DefaultRevisionEntity) r[1]).getTimestamp())
            );
        })
        .toList();
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
    @RequestBody PatchPolicyRequest patchPolicyRequest,
    @AuthenticationPrincipal Jwt jwt) {
      String sub = jwt.getSubject();
      return ResponseEntity.ok(policyService.updatePolicy(sub, patchPolicyRequest.getPolicy(), patchPolicyRequest.getRejection()));
    }

  @GetMapping("/review")
  public ResponseEntity<Set<PolicyDTO>> getReviewPoliciesForManager(@AuthenticationPrincipal Jwt jwt) {
    String sub = jwt.getSubject();
    return ResponseEntity.ok(policyService.getPoliciesToReviewForPolicyManager(sub));
  }

  @GetMapping("/rejected")
  public ResponseEntity<Set<PolicyRejectionDTO>> getRejectedPoliciesForManager(@AuthenticationPrincipal Jwt jwt) {
    String sub = jwt.getSubject();
    return ResponseEntity.ok(policyService.getRejectedPoliciesForManager(sub));
  }
}
