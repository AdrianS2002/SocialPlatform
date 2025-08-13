package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.service.model.User;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserService {

    @Transactional
    User save(User user);

    @Transactional
    User updateProfile(Long id, String firstName, String lastName, String bio);

    @Transactional
    User findById(Long id);

    @Transactional
    List<User> findAll();
}
