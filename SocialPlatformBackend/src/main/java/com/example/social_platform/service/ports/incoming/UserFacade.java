package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.persistance.model.UserEntity;
import com.example.social_platform.persistance.model.UserMapper;
import com.example.social_platform.service.model.User;
import com.example.social_platform.service.ports.outgoing.UserRepository;

import com.example.social_platform.service.ports.outgoing.AuthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserFacade implements UserService{

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    public UserFacade(UserRepository userRepository, AuthRepository authRepository) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;

    }

    @Override
    public User save(User user) {
        return userRepository.persist(user);
    }

    @Override
    public User updateProfile(Long userId,String firstName,String lastName,String bio)
    {
        User user=userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBio(bio);
        return userRepository.persist(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            authRepository.findById(user.getId()).ifPresent(auth -> {
                user.setEmail(auth.getEmail());
                user.setIsValidated(auth.getIsValidated() ? "true" : "false");
            });
        }

        return users;
    }





}
