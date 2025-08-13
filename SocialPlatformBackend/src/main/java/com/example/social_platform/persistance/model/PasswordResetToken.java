package com.example.social_platform.persistance.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity

@Data
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String code;

    private LocalDateTime createdAt;
    private boolean used;


}
