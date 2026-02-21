package com.passwordmanager.dto;

public class PasswordGenerateResponse {

    private String generatedPassword;
    private String strength;

    public PasswordGenerateResponse() {
    }

    public String getGeneratedPassword() {
        return generatedPassword;
    }

    public void setGeneratedPassword(String generatedPassword) {
        this.generatedPassword = generatedPassword;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }
}