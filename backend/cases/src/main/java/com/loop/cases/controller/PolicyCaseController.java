package com.loop.cases.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loop.cases.service.PolicyCaseService;

@RestController
@RequestMapping("/cases")
public class PolicyCaseController {

    private final PolicyCaseService policyCaseService;

    public PolicyCaseController(PolicyCaseService policyCaseService) {
        this.policyCaseService = policyCaseService;
    }

    @GetMapping
    getAllPolicyCases() {
        
    }

}
