package com.loop.lifestage.repository;

import com.loop.lifestage.model.LifeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeEventRepository extends JpaRepository<LifeEvent, Long> {}
