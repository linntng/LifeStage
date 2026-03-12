package com.loop.cases.dto;

import java.util.List;

public class LifestageUserDTO {
  private String id;
  private String username;
  private List<Long> lifeEventIds;
  private String role;

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

  public List<Long> getLifeEventIds() {
    return lifeEventIds;
  }

  public void setLifeEventIds(List<Long> lifeEventIds) {
    this.lifeEventIds = lifeEventIds;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}