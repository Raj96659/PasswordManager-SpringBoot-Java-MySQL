package com.passwordmanager.dto;

public class VaultBackupEntry {

    private String accountName;
    private String website;
    private String username;
    private String password;
    private String category;

    // Default constructor
    public VaultBackupEntry() {
    }

    // All-args constructor
    public VaultBackupEntry(String accountName,
                            String website,
                            String username,
                            String password,
                            String category) {
        this.accountName = accountName;
        this.website = website;
        this.username = username;
        this.password = password;
        this.category = category;
    }

    // Getters and Setters

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}