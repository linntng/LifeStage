package com.loop.cases.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loop.cases.model.PolicyCase;

public interface PolicyCaseRepository extends JpaRepository<PolicyCase, Long> {}
