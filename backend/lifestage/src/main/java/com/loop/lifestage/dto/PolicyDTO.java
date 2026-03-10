package com.loop.lifestage.dto;

import com.loop.lifestage.model.policy.PolicyStatus;
import java.util.Set;

public class PolicyDTO {

  private Long id;
  private String name;
  private float premium;
  private PolicyStatus status;
  private Set<Long> coveredLifeEvents;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getPremium() {
    return premium;
  }

  public void setPremium(float premium) {
    this.premium = premium;
  }

  public PolicyStatus getStatus() {
    return status;
  }

  public void setStatus(PolicyStatus status) {
    this.status = status;
  }

  public Set<Long> getCoveredLifeEvents() {
    return coveredLifeEvents;
  }

  public void setCoveredLifeEvents(Set<Long> coveredLifeEvents) {
    this.coveredLifeEvents = coveredLifeEvents;
  }
}
