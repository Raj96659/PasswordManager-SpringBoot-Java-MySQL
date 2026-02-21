package com.passwordmanager.dto;

import java.util.List;

public class RegisterRequest {

    private String email;
    private String username;
    private String password;
    private String name;
    private String phone;
    private List<SecurityQuestionDTO> securityQuestions;

    // Default constructor
    public RegisterRequest() {
    }

    // All-args constructor
    public RegisterRequest(String email,
                           String username,
                           String password,
                           String name,
                           String phone,
                           List<SecurityQuestionDTO> securityQuestions) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.securityQuestions = securityQuestions;
    }

    // Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<SecurityQuestionDTO> getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(List<SecurityQuestionDTO> securityQuestions) {
        this.securityQuestions = securityQuestions;
    }
}