package com.passwordmanager.dto;



import java.util.List;

public class SecurityAuditResponse {

    private int weakCount;
    private int reusedCount;
    private int oldCount;

    private List<String> weakAccounts;
    private List<String> reusedAccounts;
    private List<String> oldAccounts;

    public int getWeakCount() {
        return weakCount;
    }

    public void setWeakCount(int weakCount) {
        this.weakCount = weakCount;
    }

    public int getReusedCount() {
        return reusedCount;
    }

    public void setReusedCount(int reusedCount) {
        this.reusedCount = reusedCount;
    }

    public int getOldCount() {
        return oldCount;
    }

    public void setOldCount(int oldCount) {
        this.oldCount = oldCount;
    }

    public List<String> getWeakAccounts() {
        return weakAccounts;
    }

    public void setWeakAccounts(List<String> weakAccounts) {
        this.weakAccounts = weakAccounts;
    }

    public List<String> getReusedAccounts() {
        return reusedAccounts;
    }

    public void setReusedAccounts(List<String> reusedAccounts) {
        this.reusedAccounts = reusedAccounts;
    }

    public List<String> getOldAccounts() {
        return oldAccounts;
    }

    public void setOldAccounts(List<String> oldAccounts) {
        this.oldAccounts = oldAccounts;
    }

    private int securityScore;

    public int getSecurityScore() {
        return securityScore;
    }

    public void setSecurityScore(int securityScore) {
        this.securityScore = securityScore;
    }
}