package com.loop.lifestage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

  @Id
  @Column(nullable = false)
  private String id;

  @Column(nullable = false)
  private String username;

  @ManyToMany
  @JoinTable(
      name = "user_life_events",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "life_event_id"))
  private List<LifeEvent> lifeEvents;

  public User() {}

  public User(String id, String username, List<LifeEvent> lifeEvents) {
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

  public List<LifeEvent> getLifeEvents() {
    return lifeEvents;
  }

  public void setLifeEvents(List<LifeEvent> lifeEvents) {
    this.lifeEvents = lifeEvents;
  }

  public void addLifeEvent(LifeEvent lifeEvent) {
    this.lifeEvents.add(lifeEvent);
  }
}
