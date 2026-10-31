package com.jg.sistema.user.application.port.in;

import com.jg.sistema.user.application.command.ChangePasswordCommand;

import java.util.UUID;

public interface UpdateUserUseCase {
    void toggleUserStatus(UUID id);
    void changePassword(ChangePasswordCommand newPassword);
}
