package com.loop.lifestage.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loop.lifestage.model.policy.PolicyRejection;

@Repository
public interface PolicyRejectionRepository extends JpaRepository<PolicyRejection, Long> {

    Set<PolicyRejection> findByAuthorId(String authorId);
}