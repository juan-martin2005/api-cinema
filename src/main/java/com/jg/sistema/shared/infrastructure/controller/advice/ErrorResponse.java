package com.jg.sistema.shared.infrastructure.controller.advice;

public record ErrorResponse(String error,Object message, int status) {
}
