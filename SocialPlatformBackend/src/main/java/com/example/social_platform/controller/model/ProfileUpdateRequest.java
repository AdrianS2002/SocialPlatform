package com.example.social_platform.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileUpdateRequest {
    @NotBlank
    @Size(max=50)
    private String firstName;

    @NotBlank
    @Size(max=30)
    private String lastName;

    @Size(max=200)
    private String bio;
}
