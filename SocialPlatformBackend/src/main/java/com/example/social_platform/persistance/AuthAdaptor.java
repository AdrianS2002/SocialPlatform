package com.example.social_platform.persistance;

import com.example.social_platform.persistance.model.AuthEntity;
import com.example.social_platform.service.model.Auth;
import com.example.social_platform.service.ports.outgoing.AuthRepository;
import com.example.social_platform.persistance.model.AuthMapper;
import com.example.social_platform.persistance.repository.AuthPsqlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthAdaptor implements AuthRepository {

    private final AuthMapper userMapper;
    private final AuthPsqlRepository userRepository;

    public AuthAdaptor(AuthMapper userMapper, AuthPsqlRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Auth persist(Auth u) {
        return userMapper.fromEntity(userRepository.save(userMapper.fromDomain(u)));
    }

    @Override
    public Optional<Auth> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::fromEntity);
    }

    @Override
    public Optional<Auth> findById(Long id) {
        return userRepository.findById(id).map(userMapper::fromEntity);
    }

    @Override
    public void save(Auth auth) {
        userRepository.save(userMapper.fromDomain(auth));
    }

    @Override
    public void updatePassword(String email, String password) {
        Optional<AuthEntity> auth = userRepository.findByEmail(email);
        if (auth.isPresent()) {
            AuthEntity authEntity = auth.get();
            authEntity.setPassword(password);
            userRepository.save(authEntity);
        }
    }
}
