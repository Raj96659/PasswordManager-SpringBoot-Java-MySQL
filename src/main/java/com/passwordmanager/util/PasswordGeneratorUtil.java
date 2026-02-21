package com.passwordmanager.util;

import java.security.SecureRandom;

public class PasswordGeneratorUtil {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    public static String generatePassword(
            int length,
            boolean useUpper,
            boolean useLower,
            boolean useNumbers,
            boolean useSpecial) {

        StringBuilder pool = new StringBuilder();

        if (useUpper) pool.append(UPPER);
        if (useLower) pool.append(LOWER);
        if (useNumbers) pool.append(NUMBERS);
        if (useSpecial) pool.append(SPECIAL);

        if (pool.length() == 0) {
            throw new RuntimeException("Select at least one character type");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(pool.length());
            password.append(pool.charAt(index));
        }

        return password.toString();
    }
}
