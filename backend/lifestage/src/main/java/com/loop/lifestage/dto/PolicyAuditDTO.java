package com.loop.lifestage.dto;

import java.time.Instant;

public record PolicyAuditDTO(
    PolicyDTO policy,
    String action,
    Instant timestamp
) {}