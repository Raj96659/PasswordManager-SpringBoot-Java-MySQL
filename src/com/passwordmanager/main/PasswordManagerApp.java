package com.passwordmanager.main;

import com.passwordmanager.model.User;
import com.passwordmanager.service.UserService;
import com.passwordmanager.service.PasswordService;

import java.util.Scanner;

public class PasswordManagerApp {

    private static UserService userService = new UserService(); // ✅ class-level

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();

        System.out.println("==== PASSWORD MANAGER ====");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Forgot Password");


        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Master Password: ");
            String password = sc.nextLine();

            userService.register(name, email, password);
        } else if (choice == 2) {
            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Master Password: ");
            String password = sc.nextLine();

            User loggedInUser = userService.login(email, password);

            if (loggedInUser != null) {
                showDashboard(loggedInUser);   // ✅ ADDED
            }
        } else if (choice == 3) {
            System.out.print("Enter email: ");
            String email = sc.nextLine();

            userService.resetPassword(email);
        }
    }

    // ================= DASHBOARD =================

    private static void showDashboard(User user) {

        Scanner sc = new Scanner(System.in);
        PasswordService passwordService = new PasswordService();

        while (true) {
            System.out.println("\n==== PASSWORD VAULT ====");
            System.out.println("1. Add Password");
            System.out.println("2. List Accounts");
            System.out.println("3. View Password");
            System.out.println("4. Update Password");
            System.out.println("5. Delete Password");
            System.out.println("6. Search Account");
            System.out.println("7. Generate Strong Password");
            System.out.println("8. Update Profile");
            System.out.println("9. Logout");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Account Name: ");
                    String acc = sc.nextLine();

                    System.out.print("Username: ");
                    String uname = sc.nextLine();

                    System.out.print("Password: ");
                    String pass = sc.nextLine();

                    passwordService.addPassword(
                            user.getId(), acc, uname, pass
                    );
                    break;

                case 2:
                    passwordService.listPasswords(user.getId());
                    break;

                case 3:
                    System.out.print("Account Name: ");
                    String accName = sc.nextLine();

                    System.out.print("Re-enter Master Password: ");
                    String mp = sc.nextLine();

                    passwordService.viewPassword(
                            user.getId(),
                            accName,
                            mp,
                            user.getMasterPassword()
                    );
                    break;

                case 4:
                    System.out.print("Account Name: ");
                    String accUpd = sc.nextLine();

                    System.out.print("New Password: ");
                    String newPass = sc.nextLine();

                    passwordService.updatePassword(user.getId(), accUpd, newPass);
                    break;

                case 5:
                    System.out.print("Account Name: ");
                    String accDel = sc.nextLine();

                    passwordService.deletePassword(user.getId(), accDel);
                    break;


                case 6:
                    System.out.print("Search keyword: ");
                    String key = sc.nextLine();

                    passwordService.searchPasswords(user.getId(), key);
                    break;

                case 7:
                    passwordService.generatePassword();
                    break;

                case 8:
                    userService.updateProfile(user);
                    break;

                case 9:
                    System.out.println("👋 Logged out successfully");
                    return;   // exits showDashboard() and goes back to main


                default:
                    System.out.println("❌ Invalid option");
            }
        }
    }
}
