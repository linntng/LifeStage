package com.loop.lifestage.service;

import java.time.Instant;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.loop.lifestage.dto.AuditRequestDTO;
import com.loop.lifestage.model.audit.AuditAccessLog;
import com.loop.lifestage.repository.AuditRepository;
import com.loop.lifestage.repository.UserRepository;

@Service
public class AuditService {
    private final AuditRepository auditAccessLogRepository;
    private final UserRepository userRepository;

    public AuditService(AuditRepository auditAccessLogRepository, UserRepository userRepository) { 
        this.auditAccessLogRepository = auditAccessLogRepository;
        this.userRepository = userRepository;
    }

    public void logAccess(String action, String entity) {
        String actorUserId = getCurrentUserId();
        String role = getCurrentUserRole(actorUserId);

        saveLog(actorUserId, role, action, entity);
    }

    public void logAccessFromOtherService(AuditRequestDTO request) {
        String role = getCurrentUserRole(request.actorUserId());

        saveLog(
            request.actorUserId(), 
            role, 
            request.action(), 
            request.entity());
    }

    private void saveLog(String actorUserId, String role, String action, String entity) {
         AuditAccessLog log = new AuditAccessLog();
        log.setActorUserId(actorUserId);
        log.setRole(role);
        log.setAction(action);
        log.setEntity(entity);
        log.setTimestamp(Instant.now());

        auditAccessLogRepository.save(log);
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private String getCurrentUserRole(String actorUserId) {
        if (actorUserId == null) {
            return "ANONYMOUS";
        }

        return userRepository.findById(actorUserId)
        .map(user -> user.getRole().name())
        .orElse("UNKNOWN");
        
       
    }
    
}
