package com.loop.lifestage.controller;

import com.loop.lifestage.dto.UserDTO;
import com.loop.lifestage.model.user.UserRole;
import com.loop.lifestage.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUsers(@AuthenticationPrincipal Jwt jwt) {
    String sub = jwt.getSubject();
    return ResponseEntity.ok(userService.getAllUsers(sub));
  }

  @GetMapping("/{id}")
  @PreAuthorize("#id == authentication.token.claims['sub']")
  public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @PostMapping
  public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {

    return ResponseEntity.ok(userService.createUser(userDTO));
  }

  @PostMapping("/{id}/lifeevents")
  @PreAuthorize("#id == authentication.token.claims['sub']")
  public ResponseEntity<UserDTO> addLifeEventToUser(
      @PathVariable String id, @RequestBody Long lifeEventId) {
    UserDTO userDTO = userService.getUserById(id);
    return ResponseEntity.ok(userService.addLifeEventToUser(userDTO, lifeEventId));
  }

  @PatchMapping("/{id}/lifeevents/{lifeEventId}")
  @PreAuthorize("#id == authentication.token.claims['sub']")
  public ResponseEntity<UserDTO> removeLifeEventForUser(
      @PathVariable String id, @PathVariable Long lifeEventId) {
    UserDTO userDTO = userService.getUserById(id);
    return ResponseEntity.ok(userService.removeLifeEventForUser(userDTO, lifeEventId));
  }

  @PostMapping("/{id}/policies")
  @PreAuthorize("#id == authentication.token.claims['sub']")
  public ResponseEntity<UserDTO> addPolicyToUser(
      @PathVariable String id, @RequestBody Long policyId) {

    UserDTO userDTO = userService.getUserById(id);
    return ResponseEntity.ok(userService.addPolicyToUser(userDTO, policyId));
  }

  @PatchMapping("/{id}/policies/{policyId}")
  @PreAuthorize("#id == authentication.token.claims['sub']")
  public ResponseEntity<UserDTO> removePolicyForUser(
      @PathVariable String id, @PathVariable Long policyId) {

    UserDTO userDTO = userService.getUserById(id);
    return ResponseEntity.ok(userService.removePolicyForUser(userDTO, policyId));
  }

  @PatchMapping("/{id}/role")
  public ResponseEntity<UserDTO> changeRoleOfUser(
      @PathVariable String id, @RequestBody String role, @AuthenticationPrincipal Jwt jwt) {
    String sub = jwt.getSubject();
    return ResponseEntity.ok(userService.changeRoleOfUser(id, sub, UserRole.valueOf(role)));
  }
}
