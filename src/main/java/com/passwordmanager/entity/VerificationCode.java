package com.passwordmanager.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private boolean used;
    private LocalDateTime expiryTime;

    @ManyToOne
    private User user;

    // Default constructor (required by JPA)
    public VerificationCode() {
    }

    // All-args constructor
    public VerificationCode(Long id, String code, boolean used,
                            LocalDateTime expiryTime, User user) {
        this.id = id;
        this.code = code;
        this.used = used;
        this.expiryTime = expiryTime;
        this.user = user;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}