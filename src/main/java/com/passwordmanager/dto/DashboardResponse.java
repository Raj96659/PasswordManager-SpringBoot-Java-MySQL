package com.passwordmanager.dto;

public class DashboardResponse {

    private int totalPasswords;
    private int weakPasswords;
    private int reusedPasswords;
    private int favoritePasswords;
    private int strongPasswords;

    // Default constructor
    public DashboardResponse() {
    }

    // All-args constructor
    public DashboardResponse(int totalPasswords,
                             int weakPasswords,
                             int reusedPasswords,
                             int oldPasswords,
                             int favoritePasswords) {
        this.totalPasswords = totalPasswords;
        this.weakPasswords = weakPasswords;
        this.reusedPasswords = reusedPasswords;
        this.favoritePasswords = favoritePasswords;
    }

    // Getters and Setters

    public int getTotalPasswords() {
        return totalPasswords;
    }

    public void setTotalPasswords(int totalPasswords) {
        this.totalPasswords = totalPasswords;
    }

    public int getWeakPasswords() {
        return weakPasswords;
    }

    public void setWeakPasswords(int weakPasswords) {
        this.weakPasswords = weakPasswords;
    }

    public int getReusedPasswords() {
        return reusedPasswords;
    }

    public void setReusedPasswords(int reusedPasswords) {
        this.reusedPasswords = reusedPasswords;
    }

    public int getFavoritePasswords() {
        return favoritePasswords;
    }

    public void setFavoritePasswords(int favoritePasswords) {
        this.favoritePasswords = favoritePasswords;
    }

    public int getStrongPasswords() {
        return strongPasswords;
    }

    public void setStrongPasswords(int strongPasswords) {
        this.strongPasswords = strongPasswords;
    }
}