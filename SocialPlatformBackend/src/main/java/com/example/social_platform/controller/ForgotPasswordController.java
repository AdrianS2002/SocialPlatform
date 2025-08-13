package com.example.social_platform.controller;

import com.example.social_platform.controller.model.ResetPasswordRequest;
import com.example.social_platform.controller.model.VerificationRequest;
import com.example.social_platform.service.ports.incoming.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable String email) {
        boolean sent = forgotPasswordService.generateAndSendCode(email);

        if (sent) {
            return ResponseEntity.ok("Recovery code sent to " + email);
        } else {
            return ResponseEntity.status(500).body("Failed to send recovery code.");
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest request) {
        boolean isValid = forgotPasswordService.verifyCode(request.getEmail(), request.getCode());
        if (isValid) {
            return ResponseEntity.ok("Code verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired code");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request)
    {
        boolean reset=forgotPasswordService.resetPassword(request.getEmail(),request.getNewPassword());
        if (reset) {
            return ResponseEntity.ok("Password reset successfully");
        }
        else {return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired password");}
    }



}

