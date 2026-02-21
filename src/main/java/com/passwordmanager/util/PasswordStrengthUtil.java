package com.passwordmanager.util;

public class PasswordStrengthUtil {

    public static String checkStrength(String password) {

        int score = 0;

        if (password.length() >= 8) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[!@#$%^&*()].*")) score++;

        if (score <= 2) return "Weak";
        if (score == 3) return "Medium";
        if (score == 4) return "Strong";
        return "Very Strong";
    }
}
