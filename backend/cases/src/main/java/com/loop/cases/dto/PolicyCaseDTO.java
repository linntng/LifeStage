package com.loop.cases.dto;

import com.loop.cases.model.PolicyCaseStatus;

public class PolicyCaseDTO {
    private String userId;
    private Long policyId;
    private PolicyCaseStatus status;

    public PolicyCaseDTO(String userId, Long policyId, PolicyCaseStatus status) {
        this.userId = userId;
        this.policyId = policyId;
        this.status = status;
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


