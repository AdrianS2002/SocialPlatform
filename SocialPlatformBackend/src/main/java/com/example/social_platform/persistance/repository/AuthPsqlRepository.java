package com.example.social_platform.persistance.repository;

import com.example.social_platform.persistance.model.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthPsqlRepository extends JpaRepository<AuthEntity, Long> {

    Optional<AuthEntity> findByEmail(String email);
    Optional<AuthEntity> findById(Long id);

}

