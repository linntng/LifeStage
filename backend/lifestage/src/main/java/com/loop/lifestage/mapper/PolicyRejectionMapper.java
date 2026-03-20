package com.loop.lifestage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.loop.lifestage.dto.PolicyRejectionDTO;
import com.loop.lifestage.model.policy.Policy;
import com.loop.lifestage.model.policy.PolicyRejection;

@Mapper(componentModel = "spring")
public interface PolicyRejectionMapper {

    // Entity → DTO
    @Mapping(source = "rejectedPolicy.id", target = "rejectedPolicyId")
    PolicyRejectionDTO toDto(PolicyRejection entity);

    // DTO → Entity
    @Mapping(source = "rejectedPolicyId", target = "rejectedPolicy")
    PolicyRejection toEntity(PolicyRejectionDTO dto);

    // Helper method for ID → Policy
    default Policy map(Long id) {
        if (id == null) return null;

        Policy policy = new Policy();
        policy.setId(id);
        return policy;
    }
}