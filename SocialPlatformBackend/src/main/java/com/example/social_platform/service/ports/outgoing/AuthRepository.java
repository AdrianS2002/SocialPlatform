package com.example.social_platform.service.ports.outgoing;

import com.example.social_platform.service.model.Auth;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface AuthRepository {
    @Transactional
    Auth persist(Auth u);

    @Transactional
    Optional<Auth> findByEmail(String email);

    @Transactional
    Optional<Auth> findById(Long id);

    @Transactional
    void save(Auth auth);

    @Transactional
    void updatePassword(String email, String password);
}
