package com.passwordmanager.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PasswordEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountName;
    private String website;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @Column(length = 1000)
    private String encryptedPassword;

    private String category;
    private boolean favorite;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Default constructor (required by JPA)
    public PasswordEntry() {
    }

    // All-args constructor
    public PasswordEntry(Long id, String accountName, String website, String username,
                         String encryptedPassword, String category, boolean favorite,
                         LocalDateTime createdAt, LocalDateTime updatedAt, User user) {
        this.id = id;
        this.accountName = accountName;
        this.website = website;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.category = category;
        this.favorite = favorite;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEncryptedPassword() { return encryptedPassword; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}