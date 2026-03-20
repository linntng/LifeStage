package com.loop.lifestage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loop.lifestage.dto.AuditRequestDTO;
import com.loop.lifestage.service.AuditService;

@RestController
@RequestMapping("/internal/audit")
public class AuditController {
    private final AuditService auditService;

    public AuditController( AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping("/access")
    public ResponseEntity<Void> logAccess (@RequestBody AuditRequestDTO request) {
        auditService.logAccessFromOtherService(request);
        return ResponseEntity.ok().build();
    }
}
