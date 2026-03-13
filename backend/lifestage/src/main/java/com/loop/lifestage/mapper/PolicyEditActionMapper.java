package com.loop.lifestage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.loop.lifestage.dto.PolicyEditActionDTO;
import com.loop.lifestage.model.policy.Policy;
import com.loop.lifestage.model.policy.PolicyEditAction;
import com.loop.lifestage.repository.PolicyRepository;

@Mapper(componentModel = "spring")
public abstract class PolicyEditActionMapper {

    @Autowired
    protected PolicyRepository policyRepository;

    @Mapping(
        source = "policy",
        target = "policyId",
        qualifiedByName = "policyToId"
    )
    public abstract PolicyEditActionDTO toDto(PolicyEditAction editAction);

    @Mapping(
        source = "policyId",
        target = "policy",
        qualifiedByName = "idToPolicy"
    )
    public abstract PolicyEditAction toEntity(PolicyEditActionDTO editActionDTO);

    @Named("policyToId")
    protected Long policyToId(Policy policy) {
        return policy.getId();
    }

    @Named("idToPolicy")
    protected Policy idToPolicy(Long id) {
        return policyRepository.getReferenceById(id);
    }
}