
package com.loop.lifestage.dto;

import com.loop.lifestage.model.policy.EditAction;

public class PolicyEditActionDTO {

    private Long policyId;

    private EditAction action;

    public PolicyEditActionDTO() {}

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public EditAction getAction() {
        return action;
    }

    public void setAction(EditAction action) {
        this.action = action;
    }
}