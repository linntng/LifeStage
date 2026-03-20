package com.loop.lifestage.model.policy;

import com.loop.lifestage.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "policy_rejections")
public class PolicyRejection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User author;

    @ManyToOne
    private User reviewer;

    @OneToOne
    private Policy rejectedPolicy;

    @Column
    private String explanation;

    public PolicyRejection() {
    }

    public PolicyRejection(User author, User reviewer, Policy rejectedPolicy, String explanation) {
        this.author = author;
        this.reviewer = reviewer;
        this.rejectedPolicy = rejectedPolicy;
        this.explanation = explanation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public Policy getRejectedPolicy() {
        return rejectedPolicy;
    }

    public void setRejectedPolicy(Policy rejectedPolicy) {
        this.rejectedPolicy = rejectedPolicy;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}