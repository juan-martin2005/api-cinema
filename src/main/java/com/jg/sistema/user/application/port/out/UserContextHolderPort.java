package com.jg.sistema.user.application.port.out;

import java.util.UUID;

public interface UserContextHolderPort {
    UUID getCurrentUserId();
    default boolean isAdmin() { return false; }
}
