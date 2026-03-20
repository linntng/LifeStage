package com.loop.lifestage.repository;

import com.loop.lifestage.model.policy.Policy;
import com.loop.lifestage.model.policy.PolicyStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<Policy> findByStatus(PolicyStatus status);
}
