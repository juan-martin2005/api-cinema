package com.jg.sistema.auth.infrastructure.controller;

import com.jg.sistema.auth.application.port.in.ResendEmailVerificationCodeUseCase;
import com.jg.sistema.auth.application.port.in.VerifyEmailUseCase;
import com.jg.sistema.auth.infrastructure.contract.ApiEmailDoc;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/email")
public class EmailController implements ApiEmailDoc {

    private final VerifyEmailUseCase verifyEmailUseCase;
    private final ResendEmailVerificationCodeUseCase resendCodeUseCase;

    @Override
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String code) {
        verifyEmailUseCase.verifyEmail(email, code);
        return ResponseEntity.ok(new ApiResponseDTO("Verified email", true));
    }

    @Override
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        resendCodeUseCase.resendEmailVerificationCode(email);
        return ResponseEntity.ok(new ApiResponseDTO("Verification code resent", true));
    }
}
