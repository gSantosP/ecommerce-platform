package com.ecomm.auth.domain;

import java.util.UUID;

/**
 * User aggregate (immutable record).
 * Role is a simple enum-like string: ADMIN or CUSTOMER.
 */
public record User(
    UUID id,
    String email,
    String passwordHash,
    String name,
    String role
) {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_CUSTOMER = "CUSTOMER";

    public boolean isAdmin() {
        return ROLE_ADMIN.equals(role);
    }
}
