package com.passwordmanager.service;

import com.passwordmanager.entity.User;
import com.passwordmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    // REGISTER
    public User register(User user) {

        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Encode master password before saving
        user.setMasterPasswordHash(
                encoder.encode(user.getMasterPasswordHash())
        );

        return userRepository.save(user);
    }

    // LOGIN
    public User login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getMasterPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}