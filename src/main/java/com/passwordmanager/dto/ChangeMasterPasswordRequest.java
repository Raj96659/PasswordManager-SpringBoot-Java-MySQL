package com.passwordmanager.dto;

public class ChangeMasterPasswordRequest {

    private String currentPassword;
    private String newPassword;

    // Default constructor
    public ChangeMasterPasswordRequest() {
    }

    // All-args constructor
    public ChangeMasterPasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    // Getters and Setters

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}