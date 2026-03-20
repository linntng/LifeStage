package com.loop.cases.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loop.cases.client.LifestageClient;
import com.loop.cases.dto.AuditRequestDTO;
import com.loop.cases.dto.PolicyCaseDTO;
import com.loop.cases.model.PolicyCase;
import com.loop.cases.service.PolicyCaseService;

@RestController
@RequestMapping("/cases")
public class PolicyCaseController {

    private final PolicyCaseService policyCaseService;
    private final LifestageClient lifestageClient;

    public PolicyCaseController(PolicyCaseService policyCaseService, LifestageClient lifestageClient) {
        this.policyCaseService = policyCaseService;
        this.lifestageClient = lifestageClient;
    }

    @GetMapping
    public ResponseEntity<Set<PolicyCase>> getAllPolicyCases(
            Authentication authentication,
            @RequestHeader("Authorization") String token) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();

        lifestageClient.logAuditAccess(
        token,
        new AuditRequestDTO(
            userId,
            "READ_LIST",
            "PolicyCase"
        )
    );
            
        return ResponseEntity.ok(policyCaseService.getAllPolicyCases(userId, token));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("#id == authentication.token.claims['sub']")
    public ResponseEntity<Set<PolicyCase>> getUserPolicyCases(@RequestHeader("Authorization") String token,
            @PathVariable String id) {
        return ResponseEntity.ok(policyCaseService.getUserPolicyCases(id, token));
    }

    @PostMapping("user/{id}")
    @PreAuthorize("#id == authentication.token.claims['sub']")
    public ResponseEntity<PolicyCase> addPolicyCaseToUser(
            @PathVariable String id, @RequestBody PolicyCaseDTO policyCaseDTO) {
        return ResponseEntity.ok(policyCaseService.addPolicyCaseToUser(policyCaseDTO));
    }

    @PatchMapping("/{caseId}/status")
    public ResponseEntity<PolicyCase> updatePolicyCaseStatus(
            @PathVariable Long caseId,
            Authentication authentication,
            @RequestHeader("Authorization") String token,
            @RequestBody String status) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaim("sub");
        return ResponseEntity.ok(policyCaseService.updatePolicyCaseStatus(caseId, status, userId, token));
    }

    @DeleteMapping("/{caseId}")
    public ResponseEntity<Void> removePolicyCase(@PathVariable Long caseId) {
        policyCaseService.removePolicyCase(caseId);
        return ResponseEntity.noContent().build();
    }

}
