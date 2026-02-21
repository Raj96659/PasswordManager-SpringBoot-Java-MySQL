package com.passwordmanager.dto;

public class LoginRequest {

    private String username;
    private String masterPassword;

    // Default constructor (required for JSON binding)
    public LoginRequest() {
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }
}