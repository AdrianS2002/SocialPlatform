package com.example.social_platform.service.ports.incoming;


import com.example.social_platform.service.model.Auth;
import com.example.social_platform.service.model.User;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface AuthService {

    @Transactional
    Auth save(Auth user);

    @Transactional
    Optional<Auth> findByEmail(String email);

    @Transactional
    void validateUser(Long id);

    @Transactional
    void anonymizeUser(Long id);
    @Transactional
    Optional<User> findById(Long id);

}
