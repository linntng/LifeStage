package com.loop.lifestage.repository;

import com.loop.lifestage.model.policy.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {}
