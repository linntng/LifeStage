package com.loop.lifestage.model.policy;

import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.model.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "policies")
public class Policy {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column private float premium;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PolicyStatus status;

  @ManyToMany
  @JoinTable(
      name = "policy_life_events",
      joinColumns = @JoinColumn(name = "policy_id"),
      inverseJoinColumns = @JoinColumn(name = "life_event_id"))
  private Set<LifeEvent> coveredLifeEvents;

  @ManyToMany(mappedBy = "policies")
  private Set<User> coveredUsers;

  @Column private Boolean inReview = false;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getInReview() {
    return this.inReview;
  }

  public void setInReview(Boolean inReview) {
    this.inReview = inReview;
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

  public Set<LifeEvent> getCoveredLifeEvents() {
    return coveredLifeEvents;
  }

  public void setCoveredLifeEvents(Set<LifeEvent> coveredLifeEvents) {
    this.coveredLifeEvents = coveredLifeEvents;
  }

  public Set<User> getCoveredUsers() {
    return coveredUsers;
  }

  public void setCoveredUsers(Set<User> coveredUsers) {
    this.coveredUsers = coveredUsers;
  }

  public void addCoveredLifeEvent(LifeEvent lifeEvent) {
    this.coveredLifeEvents.add(lifeEvent);
  }

  public void addCoveredUser(User user) {
    this.coveredUsers.add(user);
  }
}
