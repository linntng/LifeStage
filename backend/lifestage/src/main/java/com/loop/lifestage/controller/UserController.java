package com.loop.lifestage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.loop.lifestage.dto.UserDTO;
import com.loop.lifestage.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @PostMapping("/{id}/lifeevents")
    public ResponseEntity<UserDTO> addLifeEventToUser(@PathVariable String id, @RequestBody Long lifeEventId) {
        UserDTO userDTO = userService.getUserById(id);
        userDTO.addLifeEvent(lifeEventId);
        return ResponseEntity.ok(userService.updateUser(userDTO));
    }

    @PatchMapping("/{id}/lifeevents/{lifeEventId}")
    public ResponseEntity<UserDTO> removeLifeEventForUser(@PathVariable String id, @PathVariable Long lifeEventId) {
        UserDTO userDTO = userService.getUserById(id);
        userDTO.removeLifeEvent(lifeEventId);
        return ResponseEntity.ok(userService.updateUser(userDTO));
    }
}