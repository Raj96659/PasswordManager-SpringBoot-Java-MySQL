package com.passwordmanager.service;

import java.util.Scanner;

import com.passwordmanager.dao.UserDao;
import com.passwordmanager.model.User;
import com.passwordmanager.util.PasswordEncryptor;

public class UserService {

    private UserDao userDao = new UserDao();

    // Registration
    public void register(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setMasterPassword(
                PasswordEncryptor.encrypt(password)
        );

        userDao.saveUser(user);
        System.out.println("User registered successfully!");
    }

    // Login
    public User login(String email, String password) {

        String encryptedPassword = PasswordEncryptor.encrypt(password);

        User user = userDao.findByEmailAndPassword(email, encryptedPassword);

        if (user == null) {
            System.out.println("❌ Invalid email or password");
            return null;
        }

        System.out.println("✅ Login successful. Welcome " + user.getName());
        return user;
    }

    public void resetPassword(String email) {

        User user = userDao.findByEmail(email);

        if (user == null) {
            System.out.println("❌ Email not found");
            return;
        }

        SecurityService securityService = new SecurityService();

        if (!securityService.verifyAnswers(user.getId())) {
            System.out.println("❌ Security answers incorrect");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new master password: ");
        String newPass = sc.nextLine();

        String encrypted = PasswordEncryptor.encrypt(newPass);

        userDao.updateMasterPassword(user.getId(), encrypted);

        System.out.println("✅ Password reset successful");
    }

    public void updateProfile(User user) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter new name: ");
        String newName = sc.nextLine();

        System.out.print("Enter new email: ");
        String newEmail = sc.nextLine();

        boolean updated = userDao.updateProfile(
                user.getId(), newName, newEmail
        );

        if (updated) {
            user.setName(newName);
            user.setEmail(newEmail);
            System.out.println("✅ Profile updated successfully");
        } else {
            System.out.println("❌ Profile update failed");
        }
    }


}
