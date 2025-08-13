package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.constants.AuthoritiesConstants;
import com.example.social_platform.service.model.Auth;
import com.example.social_platform.service.model.User;
import com.example.social_platform.service.ports.outgoing.AuthRepository;
import com.example.social_platform.service.ports.outgoing.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthFacade implements AuthService {


    private final AuthRepository authRepository;
    private final UserRepository userRepository;


    public AuthFacade(AuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Auth save(Auth user) {
        user.setRole(AuthoritiesConstants.USER);
        user.setIsValidated(Boolean.FALSE);
        Auth savedAuth = authRepository.persist(user);
        userRepository.persist(User.builder()
                        .id(savedAuth.getId())
                        .isBlocked(Boolean.FALSE)
                        .build());
        return savedAuth;
    }

    @Override
    public Optional<Auth> findByEmail(String email) {
        return authRepository.findByEmail(email);
    }

    @Override
    public void validateUser(Long id) {
        Auth auth = authRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        auth.setIsValidated(Boolean.TRUE);
        authRepository.save(auth);
    }

    @Override
    public void anonymizeUser(Long id) {
        Auth auth = authRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        auth.setEmail("unknown_user_" + id + "@nothing.com");
        auth.setPassword("");
        auth.setIsValidated(false);
        authRepository.save(auth);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName("Unknown");
        user.setLastName("User");
        user.setBio("Blocked");
        userRepository.persist(user);
    }

    @Override
    public Optional<User> findById(Long id) {

        return userRepository.findById(id);
    }

}
