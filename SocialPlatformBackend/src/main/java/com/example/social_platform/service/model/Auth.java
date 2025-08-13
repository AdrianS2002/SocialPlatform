package com.example.social_platform.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auth {

    private Long id;
    private String email;
    private String password;
    private String role;
    private Boolean isValidated;
}
