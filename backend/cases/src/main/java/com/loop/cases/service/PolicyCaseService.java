package com.loop.cases.service;

import java.util.Set;

import com.loop.cases.client.LifestageClient;
import com.loop.cases.dto.LifestageUserDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loop.cases.exception.BadRequestException;
import com.loop.cases.exception.ResourceNotFoundException;
import com.loop.cases.model.PolicyCase;
import com.loop.cases.repository.PolicyCaseRepository;

import feign.FeignException;

import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PolicyCaseService {
    
    private final PolicyCaseRepository policyCaseRepository;
    private final LifestageClient lifestageClient;

    public PolicyCaseService(
        PolicyCaseRepository policyCaseRepository,
        LifestageClient lifestageClient
        ) {
        this.policyCaseRepository = policyCaseRepository;
        this.lifestageClient = lifestageClient;
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
            LifestageUserDTO user = lifestageClient.getUserById(userId, token);
            if (!user.getRole().equals("CASE_HANDLER")) {
                throw new BadRequestException("User with id: " + userId + " does not have permission to access all policy cases");
            }
            return policyCaseRepository.findAll().stream()
                .collect(Collectors.toSet());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving users", e);
        }
    }

    @Transactional(readOnly = true)
    public Set<PolicyCase> getUserPolicyCases(String userId, String token) {
        try {
            LifestageUserDTO user = lifestageClient.getUserById(userId, token);
            return policyCaseRepository.findAll().stream()
                    .filter(policyCase -> policyCase.getUserId().equals(userId))
                    .collect(Collectors.toSet());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving users", e);
        }
    }

    @Transactional
    public PolicyCase addPolicyCaseToUser(PolicyCase policyCase) {
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
}