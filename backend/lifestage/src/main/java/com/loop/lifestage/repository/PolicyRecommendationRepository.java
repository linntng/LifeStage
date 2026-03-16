
package com.loop.lifestage.repository;

import com.loop.lifestage.model.policy.PolicyRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface PolicyRecommendationRepository extends JpaRepository<PolicyRecommendation, Long> {
    Optional<PolicyRecommendation> findTopByUserIdOrderByCreatedAtDesc(String userId);
}