package com.passwordmanager.service;

import com.passwordmanager.dto.PasswordEntryRequest;
import com.passwordmanager.dto.PasswordEntryResponse;
import com.passwordmanager.dto.PasswordViewResponse;
import com.passwordmanager.entity.PasswordEntry;
import com.passwordmanager.entity.User;
import com.passwordmanager.repository.PasswordEntryRepository;
import com.passwordmanager.repository.UserRepository;
import com.passwordmanager.util.EncryptionUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    // Add Password
    // ==========================
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

        return mapToResponse(saved);
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

    // ==========================
    // View Decrypted Password
    // ==========================
    public PasswordViewResponse viewPassword(
            Long id,
            String masterPassword,
            String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify master password
        if (!passwordEncoder.matches(masterPassword, user.getMasterPasswordHash())) {
            throw new RuntimeException("Master password incorrect");
        }

        PasswordEntry entry = passwordEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        // Ensure entry belongs to user
        if (!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        String decrypted;
        try {
            decrypted = EncryptionUtil.decrypt(entry.getEncryptedPassword());
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed");
        }

        PasswordViewResponse response = new PasswordViewResponse();
        response.setAccountName(entry.getAccountName());
        response.setUsername(entry.getUsername());
        response.setDecryptedPassword(decrypted);

        return response;
    }

    // ==========================
    // Toggle Favorite
    // ==========================
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

    // ==========================
    // Helper: Map Entity to Response
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
// Search Passwords
// ==========================
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
}