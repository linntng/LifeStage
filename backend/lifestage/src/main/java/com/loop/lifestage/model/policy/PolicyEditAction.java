package com.loop.lifestage.model.policy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
public class PolicyEditAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "policy_recommendation_id", nullable = false)
    private PolicyRecommendation policyRecommendation;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Enumerated(EnumType.STRING)
    @Column
    private EditAction action;

    public PolicyEditAction() {}

    public Long getId() {
        return id;
    }

    public PolicyRecommendation getPolicyRecommendation() {
        return policyRecommendation;
    }

    public void setPolicyRecommendation(PolicyRecommendation policyRecommendation) {
        this.policyRecommendation = policyRecommendation;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public EditAction getAction() {
        return action;
    }

    public void setAction(EditAction action) {
        this.action = action;
    }
}