package com.jg.sistema.auth.application.command;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
