
package com.loop.lifestage.controller;

import org.springframework.http.ResponseEntity;
import com.loop.lifestage.service.LifeEventService;
import com.loop.lifestage.dto.LifeEventDTO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/lifeevents")
public class LifeEventController {
    private final LifeEventService lifeEventService;

    public LifeEventController(LifeEventService lifeEventService) {
        this.lifeEventService = lifeEventService;
    }

    @GetMapping("")
    public ResponseEntity<List<LifeEventDTO>> getAllLifeEvents() {
        return ResponseEntity.ok(lifeEventService.getAllLifeEvents());
    }
}