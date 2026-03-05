package com.loop.lifestage.repository;

import com.loop.lifestage.model.LifeEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LifeEventRepository extends JpaRepository<LifeEvent, Long> {}
