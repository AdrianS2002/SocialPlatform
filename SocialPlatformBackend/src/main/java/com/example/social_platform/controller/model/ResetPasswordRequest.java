package com.example.social_platform.controller.model;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    String email;
    String newPassword;
}
