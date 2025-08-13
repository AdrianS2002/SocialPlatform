package com.example.social_platform.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private Boolean isBlocked;
    private String email;
    private String isValidated;
}
