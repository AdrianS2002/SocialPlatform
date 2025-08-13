package com.example.social_platform.service.ports.outgoing;

import com.example.social_platform.service.model.User;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    @Transactional
    User persist(User user);

    Optional<User> findById(Long userId);

    @Transactional
    List<User> findAll();
}
