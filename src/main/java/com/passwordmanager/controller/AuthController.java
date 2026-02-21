package com.passwordmanager.controller;

import com.passwordmanager.dto.ChangeMasterPasswordRequest;
import com.passwordmanager.dto.LoginRequest;
import com.passwordmanager.dto.RegisterRequest;
import com.passwordmanager.dto.UpdateProfileRequest;
import com.passwordmanager.entity.SecurityQuestion;
import com.passwordmanager.entity.User;
import com.passwordmanager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.passwordmanager.repository.SecurityQuestionRepository;
import com.passwordmanager.repository.UserRepository;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final SecurityQuestionRepository securityQuestionRepository;

    public AuthController(UserService userService, UserRepository userRepository, SecurityQuestionRepository securityQuestionRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.securityQuestionRepository = securityQuestionRepository;
    }

    // PUBLIC
//    @PostMapping("/register")
//    public User register(@RequestBody User user) {
//        return userService.register(user);
//    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return userService.register(request);
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

    @PutMapping("/change-master-password")
    public String changeMasterPassword(
            HttpServletRequest request,
            @RequestBody ChangeMasterPasswordRequest req) {

        String username =
                (String) request.getAttribute("username");

        return userService.changeMasterPassword(
                username,
                req.getCurrentPassword(),
                req.getNewPassword()
        );
    }

    @PutMapping("/profile")
    public String updateProfile(
            @RequestBody UpdateProfileRequest req) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

        return userService.updateProfile(username, req);
    }

    @GetMapping("/security-questions")
    public List<String> getQuestions() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return securityQuestionRepository
                .findByUser(user)
                .stream()
                .map(SecurityQuestion::getQuestion)
                .toList();
    }

    @PostMapping("/logout")
    public String logout() {
        return "Logout successful (delete token client-side)";
    }

}
