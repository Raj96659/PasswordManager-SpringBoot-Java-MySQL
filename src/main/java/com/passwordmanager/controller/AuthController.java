package com.passwordmanager.controller;

import com.passwordmanager.dto.LoginRequest;
import com.passwordmanager.entity.User;
import com.passwordmanager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // PUBLIC
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    // PUBLIC
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return userService.login(
                request.getUsername(),
                request.getMasterPassword());
    }

    // PUBLIC (before token issued if 2FA enabled)
    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String username,
            @RequestParam String otp) {

        return userService.verifyOtp(username, otp);
    }

    // 🔐 PROTECTED (requires JWT)
    @PutMapping("/2fa")
    public String toggle2FA(HttpServletRequest request) {

        String username =
                (String) request.getAttribute("username");

        if (username == null) {
            throw new RuntimeException("Unauthorized");
        }

        return userService.toggle2FA(username);
    }
}
