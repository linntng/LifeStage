package com.loop.lifestage.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loop.lifestage.dto.LifeEventDTO;
import com.loop.lifestage.service.AuditService;
import com.loop.lifestage.service.LifeEventService;

@RestController
@RequestMapping("/lifeevents")
public class LifeEventController {
  private final LifeEventService lifeEventService;
  private final AuditService auditService;

  public LifeEventController(LifeEventService lifeEventService, AuditService auditService) {
    this.lifeEventService = lifeEventService;
    this.auditService = auditService;
  }

  @GetMapping
  public ResponseEntity<List<LifeEventDTO>> getAllLifeEvents() {
    auditService.logAccess( 
      "READ_LIST", 
      "Life_event"
    );
    return ResponseEntity.ok(lifeEventService.getAllLifeEvents());
  }
}
