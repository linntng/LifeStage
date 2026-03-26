package com.loop.lifestage.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loop.lifestage.dto.AuditRequestDTO;
import com.loop.lifestage.dto.PolicyAuditProjection;
import com.loop.lifestage.model.audit.AuditAccessLog;
import com.loop.lifestage.repository.AuditRepository;
import com.loop.lifestage.service.AuditService;

@RestController
@RequestMapping("/internal/audit")
public class AuditController {
    private final AuditService auditService;
    private final AuditRepository auditRepository;

    public AuditController( AuditService auditService, AuditRepository auditRepository) {
        this.auditService = auditService;
        this.auditRepository = auditRepository;
    }

    @PostMapping("/access")
    public ResponseEntity<Void> logAccess (@RequestBody AuditRequestDTO request) {
        auditService.logAccessFromOtherService(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/access-log")
    public List<AuditAccessLog> getAccessLog() {
        return auditRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

     @GetMapping("/policies")
    public List<PolicyAuditProjection> getPolicyAuditLogs() {
        return auditRepository.findPolicyAuditLogs();
    }
}
