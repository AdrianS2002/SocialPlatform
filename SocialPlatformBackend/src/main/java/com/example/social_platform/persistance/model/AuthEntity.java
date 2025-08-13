package com.example.social_platform.persistance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;

@Entity
@Data
@Table(name = "auth", schema = "public")
public class AuthEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    private String role;

    @Column(name = "is_validated")
    private Boolean isValidated;
}
