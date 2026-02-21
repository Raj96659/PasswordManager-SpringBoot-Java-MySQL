package com.passwordmanager.entity;

import jakarta.persistence.*;

@Entity
public class SecurityQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String answerHash;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Default constructor (required by JPA)
    public SecurityQuestion() {
    }

    // All-args constructor
    public SecurityQuestion(Long id, String question, String answerHash, User user) {
        this.id = id;
        this.question = question;
        this.answerHash = answerHash;
        this.user = user;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerHash() {
        return answerHash;
    }

    public void setAnswerHash(String answerHash) {
        this.answerHash = answerHash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}