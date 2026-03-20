package com.loop.lifestage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loop.lifestage.model.audit.AuditAccessLog;

public interface AuditRepository extends JpaRepository<AuditAccessLog, Long>{
    
}
