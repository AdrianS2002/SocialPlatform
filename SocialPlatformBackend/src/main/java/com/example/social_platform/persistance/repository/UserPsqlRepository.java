package com.example.social_platform.persistance.repository;

import com.example.social_platform.persistance.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPsqlRepository extends JpaRepository<UserEntity, Long> {
}
