package com.passwordmanager.controller;

import com.passwordmanager.dto.*;
import com.passwordmanager.service.PasswordEntryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vault")
@PreAuthorize("hasRole('USER')")
public class PasswordController {

    private final PasswordEntryService service;

    public PasswordController(PasswordEntryService service) {
        this.service = service;
    }

    private String getUsername(HttpServletRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        return authentication.getName();
    }

    @PostMapping("/add")
    public PasswordEntryResponse addPassword(
            @RequestParam String masterPassword,
            HttpServletRequest request,
            @RequestBody PasswordEntryRequest body
            ) {

        String username = getUsername(request);
        return service.addPassword(username, masterPassword, body);
    }

    @GetMapping("/all")
    public List<PasswordEntryResponse> getAllPasswords(
            Principal principal) {

        String username = principal.getName();

        return service.getAllPasswords(username);
    }

    @GetMapping("/view/{id}")
    public PasswordViewResponse viewPassword(
            @PathVariable Long id,
            @RequestParam String masterPassword,
            HttpServletRequest request) {

        String username = getUsername(request);
        return service.viewPassword(id, masterPassword, username);
    }

    @PutMapping("/favorite/{id}")
    public String toggleFavorite(
            @PathVariable Long id,
            HttpServletRequest request) {

        String username = getUsername(request);
        return service.toggleFavorite(id, username);
    }

    @GetMapping("/search")
    public List<PasswordEntryResponse> search(
            @RequestParam String keyword,
            HttpServletRequest request) {

        String username = getUsername(request);
        return service.search(username, keyword);
    }

    @GetMapping("/audit/weak")
    public String checkWeakPasswords(
            HttpServletRequest request) {

        String username = getUsername(request);
        return service.checkWeakPasswords(username);
    }


    @GetMapping("/backup/export")
    public String exportVault(
            @RequestParam String masterPassword) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

        return service.exportVault(username, masterPassword);
    }

    @PostMapping("/backup/import")
    public String importVault(
            @RequestParam String masterPassword,
            @RequestBody String encryptedData) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

        return service.importVault(
                username,
                masterPassword,
                encryptedData);
    }


    @PostMapping("/dashboard")
    public DashboardResponse dashboard(
            @RequestBody DashboardRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();
        String masterPassword = request.getMasterPassword();

        return service.getDashboard(username, masterPassword);
    }

    @GetMapping("/favorites")
    public List<PasswordEntryResponse> favorites() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

        return service.getFavoritePasswords(username);
    }

    @GetMapping("/filter")
    public List<PasswordEntryResponse> filter(
            @RequestParam String category) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

        return service.filterByCategory(username, category);
    }

    @GetMapping("/sorted")
    public List<PasswordEntryResponse> sorted(
            @RequestParam String sortBy) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

        return service.getSorted(username, sortBy);
    }

    @GetMapping("/audit")
    public SecurityAuditResponse audit(
            @RequestParam String masterPassword) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();

        return service.getSecurityAudit(username, masterPassword);
    }

    @PutMapping("/update/{id}")
    public PasswordEntryResponse updatePassword(
            @PathVariable Long id,
            @RequestParam String masterPassword,
            @RequestBody PasswordEntryRequest body,
            HttpServletRequest request) {

        String username = getUsername(request);

        return service.updatePassword(
                id,
                username,
                masterPassword,
                body
        );
    }

}
