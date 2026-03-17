package com.loop.cases.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loop.cases.client.LifestageClient;
import com.loop.cases.dto.LifestageUserDTO;
import com.loop.cases.dto.PolicyCaseDTO;
import com.loop.cases.exception.BadRequestException;
import com.loop.cases.exception.NotAuthorizedException;
import com.loop.cases.exception.ResourceNotFoundException;
import com.loop.cases.mapper.PolicyCaseMapper;
import com.loop.cases.model.PolicyCase;
import com.loop.cases.model.PolicyCaseStatus;
import com.loop.cases.repository.PolicyCaseRepository;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PolicyCaseService {
    
    private final PolicyCaseRepository policyCaseRepository;
    private final LifestageClient lifestageClient;
    private final PolicyCaseMapper policyCaseMapper;

    public PolicyCaseService(
        PolicyCaseRepository policyCaseRepository,
        LifestageClient lifestageClient,
        PolicyCaseMapper policyCaseMapper
        ) {
        this.policyCaseRepository = policyCaseRepository;
        this.lifestageClient = lifestageClient;
        this.policyCaseMapper = policyCaseMapper;
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
            throw new RuntimeException("An error occurred while creating the policy case", e);
        }
    }

    @Transactional(readOnly = true)
    public Set<PolicyCase> getAllPolicyCases(String userId, String token) {
        authenticateCaseHandlerRole(userId, token);
        try {
            return policyCaseRepository.findAll().stream()
                .collect(Collectors.toSet());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving policy cases", e);
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
            throw new RuntimeException("An error occurred while retrieving policy cases", e);
        }
    }

    @Transactional
    public PolicyCase addPolicyCaseToUser(PolicyCaseDTO policyCaseDTO) {
        try {
            PolicyCase policyCase = policyCaseMapper.toPolicyCase(policyCaseDTO);
            return policyCaseRepository.save(policyCase);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid user data: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("User not found with id: " + policyCaseDTO.getUserId());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public PolicyCase updatePolicyCaseStatus(Long caseId, String status, String userId, String token) {
        authenticateCaseHandlerRole(userId, token);
        try {
            PolicyCase policyCase = policyCaseRepository.findById(caseId)
                .orElseThrow(() -> new EntityNotFoundException("Policy case not found with id: " + caseId));
            policyCase.setStatus(PolicyCaseStatus.valueOf(status));
            return policyCaseRepository.save(policyCase);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Policy case not found with id: " + caseId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status value: " + status);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating the policy case status", e);
        }
    }

    @Transactional
    public void removePolicyCase(Long caseId) {
        try {
            policyCaseRepository.deleteById(caseId);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Policy case not found with id: " + caseId);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while removing the policy case", e);
        }
    }

    private void authenticateCaseHandlerRole(String userId, String token) {
        LifestageUserDTO user = lifestageClient.getUserById(userId, token);
        if (!user.getRole().equals("CASE_HANDLER") && !user.getRole().equals("ADMIN")) {
            throw new NotAuthorizedException("User with id: " + userId + " does not have permission to access all policy cases");
        }
    }

}