package com.passwordmanager.service;

import com.passwordmanager.entity.User;
import com.passwordmanager.entity.VerificationCode;
import com.passwordmanager.repository.UserRepository;
import com.passwordmanager.repository.VerificationCodeRepository;
import com.passwordmanager.util.JwtUtil;
import com.passwordmanager.util.OtpUtil;
import com.passwordmanager.util.SaltUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final VerificationCodeRepository verificationCodeRepository;
    private String encryptionSalt;



    public UserService(UserRepository userRepository, PasswordEncoder encoder, VerificationCodeRepository verificationCodeRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    // REGISTER
    public User register(User user) {

        user.setMasterPasswordHash(
                encoder.encode(user.getMasterPasswordHash())
        );

        user.setEncryptionSalt(
                SaltUtil.generateSalt()
        );

        return userRepository.save(user);
    }


    // LOGIN
    public String login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getMasterPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        if (user.isTwoFactorEnabled()) {

            // 🔐 Generate OTP
            String otp = OtpUtil.generateOtp();

            VerificationCode code = new VerificationCode();
            code.setCode(otp);
            code.setUsed(false);
            code.setUser(user);
            code.setExpiryTime(LocalDateTime.now().plusMinutes(5));

            verificationCodeRepository.save(code);

            // Print OTP in terminal (simulation)
            System.out.println("OTP for user " + username + " is: " + otp);

            return "2FA required";
        }

        return JwtUtil.generateToken(username);
    }



    public String verifyOtp(String username, String otpInput) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VerificationCode code =
                verificationCodeRepository
                        .findTopByUserAndUsedFalseOrderByExpiryTimeDesc(user)
                        .orElseThrow(() ->
                                new RuntimeException("No OTP found"));

        if (code.isUsed()) {
            throw new RuntimeException("OTP already used");
        }

        if (code.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!code.getCode().equals(otpInput)) {
            throw new RuntimeException("Invalid OTP");
        }

        code.setUsed(true);
        verificationCodeRepository.save(code);

        return JwtUtil.generateToken(username);
    }

    public String toggle2FA(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setTwoFactorEnabled(!user.isTwoFactorEnabled());
        userRepository.save(user);

        return "2FA status updated to: " + user.isTwoFactorEnabled();
    }

}