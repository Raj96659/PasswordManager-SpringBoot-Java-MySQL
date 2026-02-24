package com.passwordmanager.service;

import com.passwordmanager.dto.SecurityQuestionDTO;
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
import com.passwordmanager.dto.RegisterRequest;
import com.passwordmanager.dto.UpdateSecurityAnswerRequest;
import org.springframework.transaction.annotation.Transactional;



import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

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
        @Transactional
        public String register(RegisterRequest request) {

            // 🔎 Check duplicate username
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }

            // 🔎 Check duplicate email
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }

            // 🔐 Minimum 3 security questions required
            if (request.getSecurityQuestions() == null ||
                    request.getSecurityQuestions().size() < 3) {

                throw new RuntimeException(
                        "Minimum 3 security questions required");
            }

            // 🧑 Create user
            User user = new User();
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setName(request.getName());
            user.setPhone(request.getPhone());

            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new RuntimeException("Password cannot be empty");
            }

            // 🔐 Hash master password
            user.setMasterPasswordHash(
                    encoder.encode(request.getPassword())
            );

            // 🔐 Generate encryption salt
            user.setEncryptionSalt(
                    SaltUtil.generateSalt()
            );

            user.setRole(User.Role.ROLE_USER);

            userRepository.save(user);

            // 🔥 DELETE old questions first (important)
            securityQuestionRepository.deleteByUser(user);

            // 🔐 Save security questions with hashed answers
            for (SecurityQuestionDTO dto : request.getSecurityQuestions()) {

                SecurityQuestion q = new SecurityQuestion();
                q.setQuestion(dto.getQuestion());
                q.setAnswerHash(encoder.encode(dto.getAnswer()));
                q.setUser(user);

                securityQuestionRepository.save(q);
            }

            return "User registered successfully";
        }





public Map<String, String> login(String username, String masterPassword) {

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!encoder.matches(masterPassword, user.getMasterPasswordHash())) {
        throw new RuntimeException("Invalid credentials");
    }

    if (user.isTwoFactorEnabled()) {

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

//        user.setOtp(otp);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        System.out.println("Generated OTP for " + username + ": " + otp);

        return Map.of("2fa", "required");
    }

    String token = JwtUtil.generateToken(username, "ROLE_USER");

    return Map.of("token", token);
}



    public Map<String, String> verifyOtp(String username, String otp) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otp.equals(user.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP Expired");
        }


        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        String token = JwtUtil.generateToken(username, "ROLE_USER");

        return Map.of("token", token);
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

//    public String recoverPassword(RecoveryRequest request) {
//
//        User user = userRepository.findByUsername(
//                        request.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        List<SecurityQuestion> questions =
//                securityQuestionRepository.findByUser(user);
//
//        if (questions.size() < 3) {
//            throw new RuntimeException("Security questions not configured");
//        }
//
//        for (SecurityQuestion q : questions) {
//
//            String provided =
//                    request.getAnswers().get(q.getId());
//
//            if (provided == null ||
//                    !encoder.matches(provided, q.getAnswerHash())) {
//
//                throw new RuntimeException("Security answers incorrect");
//            }
//        }
//
//        // Now reset password
//        String newSalt = SaltUtil.generateSalt();
//
//        user.setMasterPasswordHash(
//                encoder.encode(request.getNewPassword()));
//        user.setEncryptionSalt(newSalt);
//
//        userRepository.save(user);
//
//        return "Password reset successful";
//    }

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

    public String updateSecurityAnswer(
            String username,
            UpdateSecurityAnswerRequest req) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 Verify master password
        if (!encoder.matches(req.getMasterPassword(),
                user.getMasterPasswordHash())) {
            throw new RuntimeException("Master password incorrect");
        }

        SecurityQuestion q =
                securityQuestionRepository.findById(req.getQuestionId())
                        .orElseThrow(() -> new RuntimeException("Question not found"));

        // 🚨 CRITICAL SECURITY CHECK
        if (!q.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        // 🔐 Hash new answer
        q.setAnswerHash(encoder.encode(req.getNewAnswer()));
        securityQuestionRepository.save(q);

        return "Security answer updated";
    }



//    public String recoverMasterPassword(
//            String username,
//            List<String> answers,
//            String newPassword) {
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        List<SecurityQuestion> questions =
//                securityQuestionRepository.findByUser(user);
//
//        if (questions.size() != answers.size()) {
//            throw new RuntimeException("Invalid answers");
//        }
//
//        for (int i = 0; i < questions.size(); i++) {
//
//            String storedHash = questions.get(i).getAnswerHash();
//            String providedAnswer = answers.get(i);
//
//            if (!encoder.matches(providedAnswer, storedHash)) {
//                throw new RuntimeException("Incorrect answers");
//            }
//        }
//
//        user.setMasterPasswordHash(
//                encoder.encode(newPassword));
//
//        userRepository.save(user);
//
//        return "Master password reset successful";
//    }

    @Transactional
    public String recoverMasterPassword(
            String username,
            List<String> answers,
            String newPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SecurityQuestion> questions =
                securityQuestionRepository.findByUser(user);

        if (questions.size() != answers.size()) {
            throw new RuntimeException("Invalid answers");
        }

        // Ensure consistent order
        questions.sort((a, b) -> a.getId().compareTo(b.getId()));

        for (int i = 0; i < questions.size(); i++) {

            String storedHash = questions.get(i).getAnswerHash();
            String providedAnswer = answers.get(i).trim();

            if (!encoder.matches(providedAnswer, storedHash)) {
                throw new RuntimeException("Incorrect answers");
            }
        }

        user.setMasterPasswordHash(encoder.encode(newPassword));
        userRepository.save(user);

        return "Master password reset successful";
    }

}