package com.jg.sistema.user.application.command;

public record ChangePasswordCommand(
        String currentPassword,
        String newPassword,
        String confirmNewPassword
) {
}
