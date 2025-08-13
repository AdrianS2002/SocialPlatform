package com.example.social_platform.persistance;


import com.example.social_platform.persistance.model.UserMapper;
import com.example.social_platform.persistance.repository.UserPsqlRepository;
import com.example.social_platform.service.model.User;
import com.example.social_platform.service.ports.outgoing.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAdapter implements UserRepository {

    private final UserPsqlRepository userPsqlRepository;
    private final UserMapper userMapper;

    public UserAdapter(UserPsqlRepository userPsqlRepository, UserMapper userMapper) {
        this.userPsqlRepository = userPsqlRepository;
        this.userMapper = userMapper;
    }


    @Override
    public User persist(User user) {
        return userMapper.fromEntity(userPsqlRepository.save(userMapper.fromDomain(user)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userPsqlRepository.findById(id).map(userMapper::fromEntity);
    }

    @Override
    public List<User> findAll() {
        return userPsqlRepository.findAll().stream().map(userMapper::fromEntity).toList();
    }
}
