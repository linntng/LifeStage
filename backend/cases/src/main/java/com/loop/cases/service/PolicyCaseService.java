package com.loop.cases.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loop.cases.exception.BadRequestException;
import com.loop.cases.exception.ResourceNotFoundException;
import com.loop.cases.model.PolicyCase;
import com.loop.cases.repository.PolicyCaseRepository;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PolicyCaseService {
    
    private final PolicyCaseRepository policyCaseRepository;
    
    public PolicyCaseService(
        PolicyCaseRepository policyCaseRepository
        ) {
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
    public Set<PolicyCase> getAllPolicyCases(String userId, String token) {
        try {
            // TODO: Validate roles and permissions using the token and userId
            return policyCaseRepository.findAll().stream().collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving users", e);
        }
    }
}