package com.loop.cases.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loop.cases.model.PolicyCase;
import com.loop.cases.service.PolicyCaseService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cases")
public class PolicyCaseController {

    private final PolicyCaseService policyCaseService;

    public PolicyCaseController(PolicyCaseService policyCaseService) {
        this.policyCaseService = policyCaseService;
    }

    @GetMapping
    public Mono<ResponseEntity<Set<PolicyCase>>> getAllPolicyCases(Authentication authentication, @RequestHeader("Authorization") String token) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaim("sub");
        return policyCaseService.getAllPolicyCases(userId, token).map(ResponseEntity::ok);
    }
}
