package com.loop.lifestage.dto;

import com.loop.lifestage.model.user.UserRole;
import java.util.Set;

public class UserDTO {
  private String id;
  private String username;
  private Set<Long> lifeEventIds;
  private Set<Long> policyIds;
  private UserRole role;

  public UserDTO() {}

  public UserDTO(String id, String username) {
    this.id = id;
    this.username = username;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Set<Long> getLifeEventIds() {
    return lifeEventIds;
  }

  public void setLifeEventIds(Set<Long> lifeEventIds) {
    this.lifeEventIds = lifeEventIds;
  }

  public void addLifeEvent(Long lifeEventId) {
    this.lifeEventIds.add(lifeEventId);
  }

  public void removeLifeEvent(Long lifeEventId) {
    this.lifeEventIds.remove(lifeEventId);
  }

  public Set<Long> getPolicyIds() {
    return this.policyIds;
  }

  public void setPolicyIds(Set<Long> policyIds) {
    this.policyIds = policyIds;
  }

  public void addPolicyById(Long policyId) {
    this.policyIds.add(policyId);
  }

  public void removePolicyById(Long policyId) {
    this.policyIds.remove(policyId);
  }

  public UserRole getRole() {
    return this.role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }
}
