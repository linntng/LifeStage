package com.loop.lifestage.dto;

public record AuditRequestDTO(
    String actorUserId,
    String action,
    String entity
) {
    
}
