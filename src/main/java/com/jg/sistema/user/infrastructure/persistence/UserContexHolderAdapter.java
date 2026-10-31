package com.jg.sistema.user.infrastructure.persistence;
import com.jg.sistema.auth.infrastructure.persistence.mapper.UserDetailCustom;
import com.jg.sistema.user.application.port.out.UserContextHolderPort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.UUID;
@Component
public class UserContexHolderAdapter implements UserContextHolderPort {
    public UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getPrincipal() instanceof UserDetailCustom user ? user.getId() : null;
    }
    public boolean isAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }
}
