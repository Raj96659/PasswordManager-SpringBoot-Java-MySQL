package com.passwordmanager.controller;

import com.passwordmanager.dto.*;
import com.passwordmanager.service.PasswordUtilityService;
import org.springframework.web.bind.annotation.*;

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
