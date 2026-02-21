package com.passwordmanager.controller;

import com.passwordmanager.dto.PasswordGenerateRequest;
import com.passwordmanager.dto.PasswordGenerateResponse;
import com.passwordmanager.service.PasswordUtilityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utility")
public class PasswordUtilityController {

    private final PasswordUtilityService service;

    public PasswordUtilityController(PasswordUtilityService service) {
        this.service = service;
    }

    @PostMapping("/generate")
    public PasswordGenerateResponse generate(
            @RequestBody PasswordGenerateRequest request) {

        return service.generatePassword(request);
    }
}
