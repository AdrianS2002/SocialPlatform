package com.example.social_platform.controller;

import com.example.social_platform.config.SecurityUtils;
import com.example.social_platform.controller.model.FeedPostResponse;
import com.example.social_platform.persistance.model.AuthEntity;
import com.example.social_platform.persistance.repository.AuthPsqlRepository;
import com.example.social_platform.service.ports.incoming.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;
    private final AuthPsqlRepository authRepo;

    public FeedController(FeedService feedService, AuthPsqlRepository authRepo) {
        this.feedService = feedService;
        this.authRepo = authRepo;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<FeedPostResponse>> getFeedPosts() {
        Long currentUserId = getCurrentUserId();
        return ResponseEntity.ok(feedService.getNews(currentUserId));
    }

    private Long getCurrentUserId() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));
        AuthEntity user = authRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
