package com.loop.cases.dto;

import com.loop.cases.model.PolicyCaseStatus;

public class PolicyCaseDTO {
    private Long id;
    private String userId;
    private Long policyId;
    private PolicyCaseStatus status;

    public PolicyCaseDTO(Long id, String userId, Long policyId, PolicyCaseStatus status) {
        this.id = id;
        this.userId = userId;
        this.policyId = policyId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public PolicyCaseStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyCaseStatus status) {
        this.status = status;
    }
}


