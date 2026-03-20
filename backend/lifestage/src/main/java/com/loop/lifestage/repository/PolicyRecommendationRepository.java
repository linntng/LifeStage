
package com.loop.lifestage.repository;

import com.loop.lifestage.model.policy.PolicyRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyRecommendationRepository extends JpaRepository<PolicyRecommendation, Long> {
    Optional<PolicyRecommendation> findTopByUserIdOrderByCreatedAtDesc(String userId);
}