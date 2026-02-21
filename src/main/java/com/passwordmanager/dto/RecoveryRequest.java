package com.passwordmanager.dto;

import java.util.Map;

public class RecoveryRequest {

    private String username;
    private Map<Long, String> answers;
    private String newPassword;

    // Default constructor
    public RecoveryRequest() {
    }

    // All-args constructor
    public RecoveryRequest(String username,
                           Map<Long, String> answers,
                           String newPassword) {
        this.username = username;
        this.answers = answers;
        this.newPassword = newPassword;
    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<Long, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, String> answers) {
        this.answers = answers;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}