package com.passwordmanager.controller;

import com.passwordmanager.dto.PasswordEntryRequest;
import com.passwordmanager.dto.PasswordGenerateRequest;
import com.passwordmanager.dto.PasswordGenerateResponse;
import com.passwordmanager.service.PasswordEntryService;
import com.passwordmanager.service.PasswordUtilityService;
import com.passwordmanager.util.PasswordGeneratorUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utility")
public class PasswordUtilityController {

    private final PasswordUtilityService service;
    private final PasswordUtilityService passwordUtilityService;
    private final PasswordEntryService passwordEntryService;

    public PasswordUtilityController(PasswordUtilityService service, PasswordUtilityService passwordUtilityService, PasswordEntryService passwordEntryService) {
        this.service = service;
        this.passwordUtilityService = passwordUtilityService;
        this.passwordEntryService = passwordEntryService;
    }

    @PostMapping("/generate")
    public List<PasswordGenerateResponse> generate(
            @RequestBody PasswordGenerateRequest request) {

        return service.generatePasswords(request);
    }

    @PostMapping("/generate-and-save")
    public String generateAndSave(
            @RequestBody PasswordGenerateRequest req,
            @RequestParam String accountName,
            @RequestParam String website,
            @RequestParam String category,
            @RequestParam String masterPassword) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

        List<String> generated =
                PasswordGeneratorUtil.generatePasswords(
                        req.getLength(),
                        req.isUseUpper(),
                        req.isUseLower(),
                        req.isUseNumbers(),
                        req.isUseSpecial(),
                        req.isExcludeSimilar(),
                        1
                );

        String generatedPassword = generated.get(0);

        PasswordEntryRequest saveReq = new PasswordEntryRequest();
        saveReq.setAccountName(accountName);
        saveReq.setWebsite(website);

        // ⚠ IMPORTANT: This should be account username, not logged-in username
        saveReq.setUsername(req.getUsername());

        saveReq.setPassword(generatedPassword);
        saveReq.setCategory(category);

        passwordEntryService.addPassword(
                username,
                masterPassword,
                saveReq
        );

        return "Generated and saved successfully";
    }

}
