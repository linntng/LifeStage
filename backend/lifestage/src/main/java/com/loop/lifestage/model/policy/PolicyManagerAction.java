package com.loop.lifestage.model.policy;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.loop.lifestage.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "policy_manager_actions")
public class PolicyManagerAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @ManyToOne
    @JoinColumn(name = "origin_policy_id", nullable = true)
    private Policy originPolicy;

    @ManyToOne
    @JoinColumn(name = "suggested_policy_id", nullable = false)
    private Policy suggestedPolicy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PolicyManagerAction() {}

    public PolicyManagerAction(
        User manager,
        Policy originPolicy,
        Policy suggestedPolicy) {
            this.manager = manager;
            this.originPolicy = originPolicy;
            this.suggestedPolicy = suggestedPolicy;
        } 

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ===== Getters and Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Policy getOriginPolicy() {
        return originPolicy;
    }

    public void setOriginPolicy(Policy originPolicy) {
        this.originPolicy = originPolicy;
    }

    public Policy getSuggestedPolicy() {
        return suggestedPolicy;
    }

    public void setSuggestedPolicy(Policy suggestedPolicy) {
        this.suggestedPolicy = suggestedPolicy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}