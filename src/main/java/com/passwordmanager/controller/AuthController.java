package com.passwordmanager.controller;

import com.passwordmanager.dto.*;
import com.passwordmanager.entity.SecurityQuestion;
import com.passwordmanager.entity.User;
import com.passwordmanager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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


    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }


//    @PostMapping("/login")
//    public Map<String, String> login(@RequestBody LoginRequest request) {
//
//        String token = userService.login(
//                request.getUsername(),
//                request.getMasterPassword());
//
//        return Map.of("token", token);
//    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        return userService.login(
                request.getUsername(),
                request.getMasterPassword());
    }

    // PUBLIC (before token issued if 2FA enabled)
    @PostMapping("/verify-otp")
    public Map<String, String> verifyOtp(
            @RequestParam String username,
            @RequestParam String otp) {

        return userService.verifyOtp(username, otp);
    }



    @PreAuthorize("hasRole('USER')")
    @PutMapping("/2fa")
    public String toggle2FA() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

        return userService.toggle2FA(username);
    }

    @PutMapping("/change-master-password")
    public String changeMasterPassword(
            @RequestBody ChangeMasterPasswordRequest req) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

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

//    @GetMapping("/security-questions")
//    public List<String> getQuestions() {
//
//        Authentication authentication =
//                SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("Unauthorized");
//        }
//
//        String username = authentication.getName();
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return securityQuestionRepository
//                .findByUser(user)
//                .stream()
//                .map(SecurityQuestion::getQuestion)
//                .toList();
//    }

    @GetMapping("/recover/questions")
    public List<String> getQuestionsForRecovery(
            @RequestParam String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return securityQuestionRepository
                .findByUser(user)
                .stream()
                .map(SecurityQuestion::getQuestion)
                .toList();
    }

    @PostMapping("/recover")
    public String recoverMasterPassword(
            @RequestBody RecoverMasterPasswordRequest request) {

        return userService.recoverMasterPassword(
                request.getUsername(),
                request.getAnswers(),
                request.getNewPassword()
        );
    }

    @PostMapping("/logout")
    public String logout() {
        return "Logout successful (delete token client-side)";
    }

}
