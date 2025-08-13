package com.example.social_platform.controller;

import com.example.social_platform.controller.model.AuthRequest;
import com.example.social_platform.persistance.repository.AlbumPsqlRepository;
import com.example.social_platform.persistance.repository.ImagePsqlRepository;
import com.example.social_platform.persistance.repository.UserPsqlRepository;
import com.example.social_platform.service.model.Auth;
import com.example.social_platform.service.ports.incoming.AlbumService;
import com.example.social_platform.service.ports.incoming.AuthService;
import com.example.social_platform.service.ports.incoming.EmailService;
import com.example.social_platform.service.ports.incoming.ImageService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AuthService authService;
    private final EmailService emailService;
    private final ImageService imageService;
    private final AlbumService albumService;


    public AdminController(AuthService authService,  EmailService emailService, ImageService imageService, AlbumService albumService) {
        this.authService = authService;
        this.emailService = emailService;
        this.imageService = imageService;
        this.albumService = albumService;
    }

    @PostMapping("/validate-user")
    @ResponseStatus(HttpStatus.OK)
    public void validateUser(@RequestParam @NotNull Long id) {
        authService.validateUser(id);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        authService.anonymizeUser(id);
        return ResponseEntity.ok("User anonymized successfully");
    }

    @DeleteMapping("/post/delete")
    public ResponseEntity<String> deletePost(@RequestParam Long imageId) {
        String userEmail = albumService.getUserEmailByImageId(imageId);
        imageService.deletePhoto(imageId);
        emailService.sendEmail(
                userEmail,
                "Post Deletion Notification",
                "Your post was removed by the admin because it violated the platform's rules."
        );
        return ResponseEntity.ok("Post deleted and user notified");
    }




}
