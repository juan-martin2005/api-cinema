package com.jg.sistema.user.infrastructure.persistence;

import com.jg.sistema.auth.infrastructure.persistence.mapper.UserDetailCustom;
import com.jg.sistema.user.application.port.out.UserContextHolderPort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserContexHolderAdapter implements UserContextHolderPort {

    @Override
    public UUID getCurrentUserId() {
        UserDetailCustom principal = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return principal != null ? principal.getId() : null;
    }
}
