package com.example.social_platform.controller.model;

import lombok.Data;

@Data
public class VerificationRequest {
    private String email;
    private String code;
}

