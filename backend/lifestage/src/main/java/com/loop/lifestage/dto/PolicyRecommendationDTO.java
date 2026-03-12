package com.loop.lifestage.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PolicyRecommendationDTO {

    private Long id;
    private float premiumImpact;

    private Long userId;
    private Long lifeEventId;

    private Set<Long> policyEditActionIds = new HashSet<>();

    private LocalDateTime createdAt;

    public PolicyRecommendationDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getPremiumImpact() {
        return premiumImpact;
    }

    public void setPremiumImpact(float premiumImpact) {
        this.premiumImpact = premiumImpact;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLifeEventId() {
        return lifeEventId;
    }

    public void setLifeEventId(Long lifeEventId) {
        this.lifeEventId = lifeEventId;
    }

    public Set<Long> getPolicyEditActionIds() {
        return policyEditActionIds;
    }

    public void setPolicyEditActionIds(Set<Long> policyEditActionIds) {
        this.policyEditActionIds = policyEditActionIds;
    }

    public void addPolicyEditActionId(Long editActionId) {
        this.policyEditActionIds.add(editActionId);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}