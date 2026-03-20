package com.loop.lifestage.model.user;

import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.model.policy.Policy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "users")
public class User {

  @Id
  @Column(nullable = false)
  private String id;

  @Column(nullable = false)
  private String username;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @ManyToMany
  @JoinTable(
      name = "user_life_events",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "life_event_id"))
  private Set<LifeEvent> lifeEvents;

  @ManyToMany
  @JoinTable(
      name = "user_policies",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "policy_id"))
  private Set<Policy> policies;

  public User() {}

  public User(String id, String username, Set<LifeEvent> lifeEvents) {
    this.id = id;
    this.username = username;
    this.lifeEvents = lifeEvents;
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

  public Set<LifeEvent> getLifeEvents() {
    return lifeEvents;
  }

  public void setLifeEvents(Set<LifeEvent> lifeEvents) {
    this.lifeEvents = lifeEvents;
  }

  public void addLifeEvent(LifeEvent lifeEvent) {
    this.lifeEvents.add(lifeEvent);
  }

  public Set<Policy> getPolicies() {
    return this.policies;
  }

  public void setPolicies(Set<Policy> policies) {
    this.policies = policies;
  }

  public void addPolicy(Policy policy) {
    this.policies.add(policy);
  }

  public UserRole getRole() {
    return this.role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }
}
