package com.passwordmanager.dto;

public class DashboardResponse {

    private int totalPasswords;
    private int weakPasswords;
    private int reusedPasswords;
    private int oldPasswords;
    private int favoritePasswords;

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
        this.oldPasswords = oldPasswords;
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

    public int getOldPasswords() {
        return oldPasswords;
    }

    public void setOldPasswords(int oldPasswords) {
        this.oldPasswords = oldPasswords;
    }

    public int getFavoritePasswords() {
        return favoritePasswords;
    }

    public void setFavoritePasswords(int favoritePasswords) {
        this.favoritePasswords = favoritePasswords;
    }
}