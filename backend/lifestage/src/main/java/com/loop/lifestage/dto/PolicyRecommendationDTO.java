package com.loop.lifestage.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PolicyRecommendationDTO {

    private float premiumImpact;

    private String userId;
    private Long lifeEventId;

    private Set<PolicyEditActionDTO> policyEditActions = new HashSet<>();

    private LocalDateTime createdAt;

    public PolicyRecommendationDTO() {}

    public float getPremiumImpact() {
        return premiumImpact;
    }

    public void setPremiumImpact(float premiumImpact) {
        this.premiumImpact = premiumImpact;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getLifeEventId() {
        return lifeEventId;
    }

    public void setLifeEventId(Long lifeEventId) {
        this.lifeEventId = lifeEventId;
    }

    public Set<PolicyEditActionDTO> getPolicyEditActions() {
        return policyEditActions;
    }

    public void setPolicyEditActions(Set<PolicyEditActionDTO> policyEditActions) {
        this.policyEditActions = policyEditActions;
    }

    public void addPolicyEditAction(PolicyEditActionDTO editActionId) {
        this.policyEditActions.add(editActionId);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}