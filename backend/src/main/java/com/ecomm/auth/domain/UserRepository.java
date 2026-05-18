package com.ecomm.auth.domain;

import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port for User persistence.
 * Implementation lives in infrastructure layer.
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    boolean existsByEmail(String email);
}
