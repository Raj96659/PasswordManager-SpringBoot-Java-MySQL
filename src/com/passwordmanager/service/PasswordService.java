package com.passwordmanager.service;

import com.passwordmanager.util.RandomPasswordGenerator;
import com.passwordmanager.dao.PasswordDao;
import com.passwordmanager.model.PasswordEntry;
import com.passwordmanager.util.PasswordEncryptor;
import com.passwordmanager.util.OTPGenerator;

import java.util.List;
import java.util.Scanner;

public class PasswordService {

    private PasswordDao passwordDao = new PasswordDao();

    // Add password
    public void addPassword(int userId, String account, String username, String password) {

        PasswordEntry entry = new PasswordEntry();
        entry.setUserId(userId);
        entry.setAccountName(account);
        entry.setUsername(username);
        entry.setEncryptedPassword(
                PasswordEncryptor.encrypt(password)
        );

        passwordDao.addPassword(entry);
    }

    // List passwords
    public void listPasswords(int userId) {

        List<PasswordEntry> entries = passwordDao.getAllPasswords(userId);

        if (entries.isEmpty()) {
            System.out.println("⚠️ No passwords found");
            return;
        }

        System.out.println("📋 Saved Accounts:");
        for (PasswordEntry e : entries) {
            System.out.println("- " + e.getAccountName() + " (" + e.getUsername() + ")");
        }
    }

    // View password (with master password + OTP validation)
    public void viewPassword(
            int userId,
            String accountName,
            String masterPasswordInput,
            String actualMasterPassword
    ) {

        String encryptedInput =
                PasswordEncryptor.encrypt(masterPasswordInput);

        if (!encryptedInput.equals(actualMasterPassword)) {
            System.out.println("❌ Master password incorrect");
            return;
        }

        // OTP flow
        String otp = OTPGenerator.generateOTP();
        System.out.println("🔐 OTP (for demo): " + otp);

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter OTP: ");
        String inputOtp = sc.nextLine();

        if (!OTPGenerator.validateOTP(inputOtp)) {
            System.out.println("❌ Invalid OTP");
            return;
        }

        PasswordEntry entry =
                passwordDao.getPasswordByAccount(userId, accountName);

        if (entry == null) {
            System.out.println("❌ Account not found");
            return;
        }

        System.out.println(
                "🔓 Password: " + entry.getEncryptedPassword()
        );
    }

    // Update password
    public void updatePassword(int userId, String account, String newPassword) {

        String encrypted = PasswordEncryptor.encrypt(newPassword);

        boolean updated = passwordDao.updatePassword(userId, account, encrypted);

        if (updated) {
            System.out.println("✅ Password updated successfully");
        } else {
            System.out.println("❌ Account not found");
        }
    }

    // delete password
    public void deletePassword(int userId, String account) {

        boolean deleted = passwordDao.deletePassword(userId, account);

        if (deleted) {
            System.out.println("🗑️ Password deleted successfully");
        } else {
            System.out.println("❌ Account not found");
        }
    }

    //searchpassword
    public void searchPasswords(int userId, String keyword) {

        List<PasswordEntry> results = passwordDao.searchPasswords(userId, keyword);

        if (results.isEmpty()) {
            System.out.println("❌ No matching accounts found");
            return;
        }

        System.out.println("🔍 Search Results:");
        for (PasswordEntry p : results) {
            System.out.println("- " + p.getAccountName() + " (" + p.getUsername() + ")");
        }
    }

    public void generatePassword() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Password length: ");
        int length = sc.nextInt();

        System.out.print("Include uppercase letters? (true/false): ");
        boolean upper = sc.nextBoolean();

        System.out.print("Include lowercase letters? (true/false): ");
        boolean lower = sc.nextBoolean();

        System.out.print("Include numbers? (true/false): ");
        boolean numbers = sc.nextBoolean();

        System.out.print("Include special characters? (true/false): ");
        boolean special = sc.nextBoolean();

        try {
            String password = RandomPasswordGenerator.generate(
                    length, upper, lower, numbers, special
            );
            System.out.println("🔐 Generated Password: " + password);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

}
