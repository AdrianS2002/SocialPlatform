package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.persistance.model.PasswordResetToken;
import com.example.social_platform.persistance.repository.PasswordResetTokenRepository;
import com.example.social_platform.service.model.Auth;
import com.example.social_platform.service.ports.outgoing.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class ForgotPasswordService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthRepository authRepository;

    
    public boolean generateAndSendCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));

        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setCode(code);
        token.setCreatedAt(LocalDateTime.now());
        token.setUsed(false);

        tokenRepository.save(token);

        String subject = "Password Reset Code";
        String message = "Your password recovery code is: " + code;

        return emailService.sendEmail(email, subject, message);
    }

    public boolean verifyCode(String email, String code) {
        PasswordResetToken token = tokenRepository.findByEmailAndCodeAndUsedFalse(email, code)
                .orElse(null);

        if (token == null) {
            return false;
        }

        token.setUsed(true);
        tokenRepository.save(token);
        return true;
    }

    public boolean resetPassword(String email, String newPassword) {
        String hashed = passwordEncoder.encode(newPassword);
        Optional<Auth> auth = authService.findByEmail(email);
        if (auth.isPresent()) {
            authRepository.updatePassword(email, hashed);
            return true;
        }
        return false;
    }



}

