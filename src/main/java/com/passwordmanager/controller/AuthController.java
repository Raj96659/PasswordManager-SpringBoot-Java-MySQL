package com.passwordmanager.controller;

import com.passwordmanager.dto.LoginRequest;
import com.passwordmanager.entity.User;
import com.passwordmanager.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return userService.login(
                request.getUsername(),
                request.getMasterPassword());
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String username,
            @RequestParam String otp) {

        return userService.verifyOtp(username, otp);
    }

    @PutMapping("/2fa")
    public String toggle2FA(@RequestParam String username) {
        return userService.toggle2FA(username);
    }
}