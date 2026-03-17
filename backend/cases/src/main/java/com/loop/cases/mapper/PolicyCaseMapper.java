package com.loop.cases.mapper;

import org.mapstruct.Mapper;

import com.loop.cases.dto.PolicyCaseDTO;
import com.loop.cases.model.PolicyCase;

@Mapper(componentModel = "spring")
public interface PolicyCaseMapper {

    PolicyCaseDTO toPolicyCaseDTO(PolicyCase policyCase);
    PolicyCase toPolicyCase(PolicyCaseDTO policyCaseDTO);
    
}
