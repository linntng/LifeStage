package com.loop.cases.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loop.cases.exception.BadRequestException;
import com.loop.cases.exception.ResourceNotFoundException;
import com.loop.cases.model.PolicyCase;
import com.loop.cases.repository.PolicyCaseRepository;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class PolicyCaseService {
    
    private final PolicyCaseRepository policyCaseRepository;
    private final UserRetrieverService userRetrieverService;
    
    public PolicyCaseService(PolicyCaseRepository policyCaseRepository, UserRetrieverService userRetrieverService) {
        this.policyCaseRepository = policyCaseRepository;
        this.userRetrieverService = userRetrieverService;
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
    public Mono<Set<PolicyCase>> getAllPolicyCases(String userId, String token) {
        try {
            return validateUserRole("CASE_HANDLER", userId, token)
                .subscribeOn(Schedulers.boundedElastic()) // Create a separate thread for performance when calling external service
                .then(Mono.just(policyCaseRepository.findAll().stream().collect(Collectors.toSet())));
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving users", e);
        }
    }
    
    private Mono<Void> validateUserRole(String requiredRole, String userId, String token) {
        return userRetrieverService.getUserRole(userId, token)
            .flatMap(userRole -> {
                 if (!requiredRole.equals(userRole)) {
                    return Mono.error(new BadRequestException("User does not have the required role: " + requiredRole));
                }
                return Mono.empty();
            }
        );
    }
}