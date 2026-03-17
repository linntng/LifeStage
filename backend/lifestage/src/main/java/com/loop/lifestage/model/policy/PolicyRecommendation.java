package com.loop.lifestage.model.policy;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.model.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "policy_recommendations")
public class PolicyRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private float premiumImpact;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "life_event_id", nullable = false)
    private LifeEvent lifeEvent;

    @OneToMany(mappedBy = "policyRecommendation", cascade = CascadeType.ALL)
    private Set<PolicyEditAction> editActions = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PolicyRecommendation() {}

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public float getPremiumImpact() {
        return premiumImpact;
    }

    public void setPremiumImpact(float premiumImpact) {
        this.premiumImpact = premiumImpact;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LifeEvent getLifeEvent() {
        return lifeEvent;
    }

    public void setLifeEvent(LifeEvent lifeEvent) {
        this.lifeEvent = lifeEvent;
    }

    public Set<PolicyEditAction> getEditActions() {
        return editActions;
    }

    public void setEditActions(Set<PolicyEditAction> editActions) {
        this.editActions = editActions;
    }

    public void addEditAction(PolicyEditAction action) {
        this.editActions.add(action);
        action.setPolicyRecommendation(this);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}