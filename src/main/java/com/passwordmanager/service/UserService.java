package com.passwordmanager.service;

import com.passwordmanager.dto.UpdateProfileRequest;
import com.passwordmanager.entity.User;
import com.passwordmanager.entity.VerificationCode;
import com.passwordmanager.repository.SecurityQuestionRepository;
import com.passwordmanager.repository.UserRepository;
import com.passwordmanager.repository.VerificationCodeRepository;
import com.passwordmanager.util.EncryptionUtil;
import com.passwordmanager.util.JwtUtil;
import com.passwordmanager.util.OtpUtil;
import com.passwordmanager.util.SaltUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.passwordmanager.dto.RecoveryRequest;
import com.passwordmanager.dto.ChangeMasterPasswordRequest;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import com.passwordmanager.entity.PasswordEntry;
import com.passwordmanager.entity.SecurityQuestion;
import com.passwordmanager.util.KeyDerivationUtil;
import com.passwordmanager.repository.PasswordEntryRepository;




import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final VerificationCodeRepository verificationCodeRepository;
    private final SecurityQuestionRepository securityQuestionRepository;
    private final PasswordEntryRepository passwordEntryRepository;





    public UserService(UserRepository userRepository, PasswordEncoder encoder, VerificationCodeRepository verificationCodeRepository, SecurityQuestionRepository securityQuestionRepository, PasswordEntryRepository passwordEntryRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.verificationCodeRepository = verificationCodeRepository;
        this.securityQuestionRepository = securityQuestionRepository;
        this.passwordEntryRepository = passwordEntryRepository;
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

    public String changeMasterPassword(
            String username,
            String currentPassword,
            String newPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(currentPassword,
                user.getMasterPasswordHash())) {

            throw new RuntimeException("Current password incorrect");
        }

        // derive old key
        SecretKeySpec oldKey =
                KeyDerivationUtil.deriveKey(
                        currentPassword,
                        user.getEncryptionSalt()
                );

        // fetch vault entries
        List<PasswordEntry> entries =
                passwordEntryRepository.findByUser(user);

        // generate new salt
        String newSalt = SaltUtil.generateSalt();

        SecretKeySpec newKey =
                KeyDerivationUtil.deriveKey(
                        newPassword,
                        newSalt
                );

        // re-encrypt all entries
        for (PasswordEntry entry : entries) {

            String decrypted =
                    EncryptionUtil.decrypt(
                            entry.getEncryptedPassword(),
                            oldKey);

            String reEncrypted =
                    EncryptionUtil.encrypt(
                            decrypted,
                            newKey);

            entry.setEncryptedPassword(reEncrypted);
        }

        // update user
        user.setMasterPasswordHash(
                encoder.encode(newPassword));
        user.setEncryptionSalt(newSalt);

        userRepository.save(user);
        passwordEntryRepository.saveAll(entries);

        return "Master password updated successfully";
    }

    public String recoverPassword(RecoveryRequest request) {

        User user = userRepository.findByUsername(
                        request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SecurityQuestion> questions =
                securityQuestionRepository.findByUser(user);

        if (questions.size() < 3) {
            throw new RuntimeException("Security questions not configured");
        }

        for (SecurityQuestion q : questions) {

            String provided =
                    request.getAnswers().get(q.getId());

            if (provided == null ||
                    !encoder.matches(provided, q.getAnswerHash())) {

                throw new RuntimeException("Security answers incorrect");
            }
        }

        // Now reset password
        String newSalt = SaltUtil.generateSalt();

        user.setMasterPasswordHash(
                encoder.encode(request.getNewPassword()));
        user.setEncryptionSalt(newSalt);

        userRepository.save(user);

        return "Password reset successful";
    }

    public String updateProfile(
            String username,
            UpdateProfileRequest req) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(req.getName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());

        userRepository.save(user);

        return "Profile updated successfully";
    }



}