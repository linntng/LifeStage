package com.loop.cases.dto;

public record AuditRequestDTO(
    String actorUserId,
    String action,
    String entity
) {
    
}
