package com.passwordmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.passwordmanager.dto.*;
import com.passwordmanager.entity.PasswordEntry;
import com.passwordmanager.entity.User;
import com.passwordmanager.repository.PasswordEntryRepository;
import com.passwordmanager.repository.UserRepository;
import com.passwordmanager.util.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordEntryService {

    private final PasswordEntryRepository passwordEntryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordEntryService(
            PasswordEntryRepository passwordEntryRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.passwordEntryRepository = passwordEntryRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================
// Add Password (Correct Secure Version)
// ==========================
    public PasswordEntryResponse addPassword(
            String username,
            String masterPassword,
            PasswordEntryRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Verify master password
        if (!passwordEncoder.matches(masterPassword,
                user.getMasterPasswordHash())) {
            throw new RuntimeException("Master password incorrect");
        }

        // ✅ Derive encryption key using real master password
        SecretKeySpec key =
                KeyDerivationUtil.deriveKey(
                        masterPassword,
                        user.getEncryptionSalt()
                );

        PasswordEntry entry = new PasswordEntry();
        entry.setAccountName(request.getAccountName());
        entry.setWebsite(request.getWebsite());
        entry.setUsername(request.getUsername());

        entry.setEncryptedPassword(
                EncryptionUtil.encrypt(request.getPassword(), key)
        );

        entry.setCategory(request.getCategory());
        entry.setFavorite(false);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());
        entry.setUser(user);

        PasswordEntry saved = passwordEntryRepository.save(entry);

        return mapToResponse(saved);
    }


    // ==========================
    // View Password
    // ==========================
    public PasswordViewResponse viewPassword(
            Long id,
            String masterPassword,
            String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(masterPassword,
                user.getMasterPasswordHash())) {
            throw new RuntimeException("Master password incorrect");
        }

        PasswordEntry entry = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        SecretKeySpec key =
                KeyDerivationUtil.deriveKey(
                        masterPassword,
                        user.getEncryptionSalt()
                );

        String decrypted =
                EncryptionUtil.decrypt(
                        entry.getEncryptedPassword(),
                        key
                );

        PasswordViewResponse response = new PasswordViewResponse();
        response.setAccountName(entry.getAccountName());
        response.setUsername(entry.getUsername());
        response.setDecryptedPassword(decrypted);

        return response;
    }

    // ==========================
    // Helper
    // ==========================
    private PasswordEntryResponse mapToResponse(PasswordEntry entry) {

        PasswordEntryResponse response = new PasswordEntryResponse();
        response.setId(entry.getId());
        response.setAccountName(entry.getAccountName());
        response.setWebsite(entry.getWebsite());
        response.setUsername(entry.getUsername());
        response.setCategory(entry.getCategory());
        response.setFavorite(entry.isFavorite());
        response.setCreatedAt(entry.getCreatedAt());

        return response;
    }

    // ==========================
// Get All Passwords
// ==========================
    public List<PasswordEntryResponse> getAllPasswords(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        List<PasswordEntryResponse> responses = new ArrayList<>();

        for (PasswordEntry entry : entries) {
            responses.add(mapToResponse(entry));
        }

        return responses;
    }

    public String toggleFavorite(Long id, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordEntry entry = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        entry.setFavorite(!entry.isFavorite());
        passwordEntryRepository.save(entry);

        return "Favorite status updated";
    }

    public List<PasswordEntryResponse> search(
            String username,
            String keyword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntry> entries =
                passwordEntryRepository
                        .findByUserAndAccountNameContaining(user, keyword);

        List<PasswordEntryResponse> responses = new ArrayList<>();

        for (PasswordEntry entry : entries) {
            responses.add(mapToResponse(entry));
        }

        return responses;
    }

    public String checkWeakPasswords(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        int weakCount = 0;

        for (PasswordEntry entry : entries) {
            if (entry.getEncryptedPassword().length() < 8) {
                weakCount++;
            }
        }

        return "Weak passwords found: " + weakCount;
    }

    public String exportVault(String username, String masterPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Verify master password
        if (!passwordEncoder.matches(masterPassword,
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Master password incorrect");
        }

        // ✅ Derive encryption key
        SecretKeySpec key =
                KeyDerivationUtil.deriveKey(
                        masterPassword,
                        user.getEncryptionSalt()
                );

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        List<VaultBackupEntry> backupList = new ArrayList<>();

        for (PasswordEntry entry : entries) {

            String decrypted =
                    EncryptionUtil.decrypt(
                            entry.getEncryptedPassword(),
                            key
                    );

            VaultBackupEntry dto = new VaultBackupEntry();
            dto.setAccountName(entry.getAccountName());
            dto.setWebsite(entry.getWebsite());
            dto.setUsername(entry.getUsername());
            dto.setPassword(decrypted);
            dto.setCategory(entry.getCategory());

            backupList.add(dto);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();

            // Convert vault to JSON
            String json = mapper.writeValueAsString(backupList);

            // Encrypt entire backup JSON again
            return EncryptionUtil.encrypt(json, key);

        } catch (Exception e) {
            throw new RuntimeException("Export failed");
        }
    }

    public String importVault(
            String username,
            String masterPassword,
            String encryptedData) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Verify master password
        if (!passwordEncoder.matches(masterPassword,
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Master password incorrect");
        }

        // ✅ Derive encryption key
        SecretKeySpec key =
                KeyDerivationUtil.deriveKey(
                        masterPassword,
                        user.getEncryptionSalt()
                );

        try {

            // ✅ Decrypt entire backup
            String json =
                    EncryptionUtil.decrypt(encryptedData, key);

            ObjectMapper mapper = new ObjectMapper();

            VaultBackupEntry[] entries =
                    mapper.readValue(json, VaultBackupEntry[].class);

            for (VaultBackupEntry dto : entries) {

                PasswordEntry entry = new PasswordEntry();
                entry.setAccountName(dto.getAccountName());
                entry.setWebsite(dto.getWebsite());
                entry.setUsername(dto.getUsername());

                // Re-encrypt using same derived key
                entry.setEncryptedPassword(
                        EncryptionUtil.encrypt(
                                dto.getPassword(), key));

                entry.setCategory(dto.getCategory());
                entry.setFavorite(false);
                entry.setCreatedAt(LocalDateTime.now());
                entry.setUpdatedAt(LocalDateTime.now());
                entry.setUser(user);

                passwordEntryRepository.save(entry);
            }

            return "Import successful";

        } catch (Exception e) {
            throw new RuntimeException("Import failed");
        }
    }

    public int countReusedPasswords(String username, String masterPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(masterPassword,
                user.getMasterPasswordHash())) {
            throw new RuntimeException("Master password incorrect");
        }

        SecretKeySpec key =
                KeyDerivationUtil.deriveKey(
                        masterPassword,
                        user.getEncryptionSalt()
                );

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        Map<String, Integer> passwordCount = new HashMap<>();

        for (PasswordEntry entry : entries) {

            String decrypted =
                    EncryptionUtil.decrypt(
                            entry.getEncryptedPassword(),
                            key);

            passwordCount.put(
                    decrypted,
                    passwordCount.getOrDefault(decrypted, 0) + 1
            );
        }

        int reused = 0;

        for (int count : passwordCount.values()) {
            if (count > 1) reused++;
        }

        return reused;
    }

    public int countOldPasswords(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        int oldCount = 0;

        for (PasswordEntry entry : entries) {

            if (entry.getUpdatedAt() != null &&
                    entry.getUpdatedAt().isBefore(
                            LocalDateTime.now().minusDays(90))) {

                oldCount++;
            }
        }

        return oldCount;
    }

}
