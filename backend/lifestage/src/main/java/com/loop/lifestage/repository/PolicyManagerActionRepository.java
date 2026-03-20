
package com.loop.lifestage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;  
import com.loop.lifestage.model.policy.PolicyManagerAction;
import org.springframework.data.repository.query.Param;
import java.util.Set;



@Repository
public interface PolicyManagerActionRepository extends JpaRepository<PolicyManagerAction, Long> {

    Optional<PolicyManagerAction> findTopBySuggestedPolicyIdOrderByCreatedAtDesc(Long suggestedPolicyId);

    @Query("""
        SELECT DISTINCT pma.suggestedPolicy.id
        FROM PolicyManagerAction pma
        WHERE pma.manager.id <> :managerId
    """)
    Set<Long> findDistinctSuggestedPolicyIdsByManagerIdNot(@Param("managerId") String managerId);
}