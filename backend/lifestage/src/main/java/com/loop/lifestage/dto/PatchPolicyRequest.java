package com.loop.lifestage.dto;

public class PatchPolicyRequest {

    private PolicyDTO policy;
    private PolicyRejectionDTO rejection;

    public PatchPolicyRequest() {}

    public PatchPolicyRequest(PolicyDTO policy, PolicyRejectionDTO rejection) {
        this.policy = policy;
        this.rejection = rejection;
    }

    public PolicyDTO getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyDTO policy) {
        this.policy = policy;
    }

    public PolicyRejectionDTO getRejection() {
        return rejection;
    }

    public void setRejection(PolicyRejectionDTO rejection) {
        this.rejection = rejection;
    }
}