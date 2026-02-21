package com.passwordmanager.controller;

import com.passwordmanager.dto.*;
import com.passwordmanager.service.PasswordEntryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vault")
public class PasswordController {

    private final PasswordEntryService service;

    public PasswordController(PasswordEntryService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public PasswordEntryResponse addPassword(
            @RequestParam String username,
            @RequestBody PasswordEntryRequest request) {

        return service.addPassword(username, request);
    }

    @GetMapping("/all")
    public List<PasswordEntryResponse> getAllPasswords(
            @RequestParam String username) {

        return service.getAllPasswords(username);
    }

    @GetMapping("/view/{id}")
    public PasswordViewResponse viewPassword(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam String masterPassword) {

        return service.viewPassword(id, masterPassword, username);
    }
}
