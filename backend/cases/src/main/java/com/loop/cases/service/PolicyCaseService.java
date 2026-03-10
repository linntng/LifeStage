package com.loop.cases.service;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.loop.cases.exception.BadRequestException;
import com.loop.cases.exception.ResourceNotFoundException;
import com.loop.cases.model.PolicyCase;
import com.loop.cases.repository.PolicyCaseRepository;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

public class PolicyCaseService {
    
    private final PolicyCaseRepository policyCaseRepository;
    
    public PolicyCaseService(PolicyCaseRepository policyCaseRepository) {
        this.policyCaseRepository = policyCaseRepository;
    }

    @Transactional
    public PolicyCase createPolicyCase(PolicyCase policyCase) {
        try {
            return policyCaseRepository.save(policyCase);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid user data: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("User not found with id: " + policyCase.getId());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating the user", e);
        }
    }

    @Transactional(readOnly = true)
    public Set<PolicyCase> getAllPolicyCases() {
        try {
            List<PolicyCase> policyCases = policyCaseRepository.findAll();
            return policyCases.stream().collect(Collectors.toSet());
            
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving users", e);
        }
    }
}
