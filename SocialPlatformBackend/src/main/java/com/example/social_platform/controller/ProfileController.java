package com.example.social_platform.controller;

import com.example.social_platform.config.SecurityUtils;
import com.example.social_platform.controller.model.ProfileUpdateRequest;
import com.example.social_platform.controller.model.UserProfileResponse;
import com.example.social_platform.service.model.Auth;
import com.example.social_platform.service.model.User;
import com.example.social_platform.service.ports.incoming.AuthService;
import com.example.social_platform.service.ports.incoming.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final AuthService authService;
    private final UserService userService;

    public ProfileController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("User not logged in"));
        Auth auth = authService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        userService.updateProfile(auth.getId(), profileUpdateRequest.getFirstName(), profileUpdateRequest.getLastName(), profileUpdateRequest.getBio());
        return ResponseEntity.ok("Profile updated successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserProfile() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));

        Auth auth = authService.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        User user = userService.findById(auth.getId());

        UserProfileResponse response = new UserProfileResponse(
                user.getFirstName(),
                user.getLastName(),
                auth.getEmail(),
                user.getBio()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getUserProfile(@RequestParam Long userId) {

        User user = userService.findById(userId);

        return ResponseEntity.ok(UserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .bio(user.getBio())
                .build());
    }


}
