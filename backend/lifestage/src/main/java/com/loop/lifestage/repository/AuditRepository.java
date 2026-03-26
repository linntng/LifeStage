package com.loop.lifestage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.loop.lifestage.dto.PolicyAuditProjection;
import com.loop.lifestage.model.audit.AuditAccessLog;

public interface AuditRepository extends JpaRepository<AuditAccessLog, Long>{

    @Query(value = """
        SELECT p.id, p.rev, p.revtype, r.revtstmp
        FROM policies_aud p
        JOIN revinfo r ON p.rev = r.rev
        ORDER BY r.revtstmp DESC
        """, nativeQuery = true)
    List<PolicyAuditProjection> findPolicyAuditLogs();
}
