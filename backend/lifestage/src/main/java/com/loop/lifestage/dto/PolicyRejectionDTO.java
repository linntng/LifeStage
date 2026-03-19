package com.loop.lifestage.dto;

public class PolicyRejectionDTO {

    private Long rejectedPolicyId;
    private String explanation;

    public PolicyRejectionDTO() {
    }

    public PolicyRejectionDTO(Long rejectedPolicyId, String explanation) {
        this.rejectedPolicyId = rejectedPolicyId;
        this.explanation = explanation;
    }

    public Long getRejectedPolicyId() {
        return rejectedPolicyId;
    }

    public void setRejectedPolicyId(Long rejectedPolicyId) {
        this.rejectedPolicyId = rejectedPolicyId;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}