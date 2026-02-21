package com.passwordmanager.service;

import com.passwordmanager.dto.*;
import com.passwordmanager.entity.PasswordEntry;
import com.passwordmanager.entity.User;
import com.passwordmanager.repository.PasswordEntryRepository;
import com.passwordmanager.repository.UserRepository;
import com.passwordmanager.util.EncryptionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordEntryService {

    private final PasswordEntryRepository passwordEntryRepository;
    private final UserRepository userRepository;

    public PasswordEntryService(
            PasswordEntryRepository passwordEntryRepository,
            UserRepository userRepository) {

        this.passwordEntryRepository = passwordEntryRepository;
        this.userRepository = userRepository;
    }

    // Add password
    public PasswordEntryResponse addPassword(
            String username,
            PasswordEntryRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordEntry entry = new PasswordEntry();
        entry.setAccountName(request.getAccountName());
        entry.setWebsite(request.getWebsite());
        entry.setUsername(request.getUsername());

        try {
            entry.setEncryptedPassword(
                    EncryptionUtil.encrypt(request.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed");
        }

        entry.setCategory(request.getCategory());
        entry.setFavorite(false);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());
        entry.setUser(user);

        PasswordEntry saved = passwordEntryRepository.save(entry);

        PasswordEntryResponse response = new PasswordEntryResponse();
        response.setId(saved.getId());
        response.setAccountName(saved.getAccountName());
        response.setWebsite(saved.getWebsite());
        response.setUsername(saved.getUsername());
        response.setCategory(saved.getCategory());
        response.setFavorite(saved.isFavorite());
        response.setCreatedAt(saved.getCreatedAt());

        return response;
    }

    // View all passwords
    public List<PasswordEntryResponse> getAllPasswords(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        List<PasswordEntryResponse> responses = new ArrayList<>();

        for (PasswordEntry entry : entries) {
            PasswordEntryResponse response =
                    new PasswordEntryResponse();

            response.setId(entry.getId());
            response.setAccountName(entry.getAccountName());
            response.setWebsite(entry.getWebsite());
            response.setUsername(entry.getUsername());
            response.setCategory(entry.getCategory());
            response.setFavorite(entry.isFavorite());
            response.setCreatedAt(entry.getCreatedAt());

            responses.add(response);
        }

        return responses;
    }

    // View decrypted password
    public PasswordViewResponse viewPassword(
            Long id,
            String masterPassword,
            String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordEntry entry =
                passwordEntryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Entry not found"));

        String decrypted;

        try {
            decrypted = EncryptionUtil.decrypt(entry.getEncryptedPassword());
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed");
        }

        PasswordViewResponse response =
                new PasswordViewResponse();

        response.setAccountName(entry.getAccountName());
        response.setUsername(entry.getUsername());
        response.setDecryptedPassword(decrypted);

        return response;
    }
}
