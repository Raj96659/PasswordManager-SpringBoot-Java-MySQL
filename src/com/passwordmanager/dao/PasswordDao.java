package com.passwordmanager.dao;

import com.passwordmanager.model.PasswordEntry;
import com.passwordmanager.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordDao {

    // Add new password
    public void addPassword(PasswordEntry entry) {

        String sql = "INSERT INTO password_entries(user_id, account_name, username, encrypted_password) VALUES(?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entry.getUserId());
            ps.setString(2, entry.getAccountName());
            ps.setString(3, entry.getUsername());
            ps.setString(4, entry.getEncryptedPassword());

            ps.executeUpdate();
            System.out.println("✅ Password saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get all passwords for a user
    public List<PasswordEntry> getAllPasswords(int userId) {

        List<PasswordEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM password_entries WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PasswordEntry p = new PasswordEntry();
                p.setId(rs.getInt("id"));
                p.setAccountName(rs.getString("account_name"));
                p.setUsername(rs.getString("username"));
                p.setEncryptedPassword(rs.getString("encrypted_password"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get password by account name
    public PasswordEntry getPasswordByAccount(int userId, String accountName) {

        String sql = "SELECT * FROM password_entries WHERE user_id=? AND account_name=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, accountName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PasswordEntry p = new PasswordEntry();
                p.setEncryptedPassword(rs.getString("encrypted_password"));
                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update existing password
    public boolean updatePassword(int userId, String accountName, String encryptedPassword) {

        String sql = "UPDATE password_entries SET encrypted_password=? WHERE user_id=? AND account_name=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, encryptedPassword);
            ps.setInt(2, userId);
            ps.setString(3, accountName);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePassword(int userId, String accountName) {

        String sql = "DELETE FROM password_entries WHERE user_id=? AND account_name=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, accountName);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // searchPassword
    public List<PasswordEntry> searchPasswords(int userId, String keyword) {

        List<PasswordEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM password_entries WHERE user_id=? AND account_name LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PasswordEntry p = new PasswordEntry();
                p.setAccountName(rs.getString("account_name"));
                p.setUsername(rs.getString("username"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
