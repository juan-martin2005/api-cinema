package com.jg.sistema.auth.infrastructure.controller;
import com.jg.sistema.auth.application.service.OAuthAccountService;
import com.jg.sistema.auth.application.command.TokenPair;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController @RequestMapping("/api/auth/oauth") @RequiredArgsConstructor
public class OAuthCodeController {
    private final OAuthAccountService accounts;
    public record Code(@NotBlank String code) {}
    @PostMapping("/exchange") public TokenPair exchange(@Valid @RequestBody Code code) { return accounts.exchange(code.code(),false); }
    @PostMapping("/link") @PreAuthorize("isAuthenticated()") public TokenPair link(@Valid @RequestBody Code code) { return accounts.exchange(code.code(),true); }
}
