package com.example.social_platform.controller;

import com.example.social_platform.config.SecurityUtils;
import com.example.social_platform.controller.model.FriendRequestWithUserRequest;
import com.example.social_platform.controller.model.FriendsRequest;
import com.example.social_platform.persistance.model.AuthEntity;
import com.example.social_platform.persistance.repository.AuthPsqlRepository;
import com.example.social_platform.service.model.FriendRequest;
import com.example.social_platform.service.ports.incoming.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/friends")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;
    private final AuthPsqlRepository authPsqlRepository;

    public FriendRequestController(FriendRequestService friendRequestService, AuthPsqlRepository authPsqlRepository) {
        this.friendRequestService = friendRequestService;
        this.authPsqlRepository = authPsqlRepository;
    }

    @PostMapping("/send-request/{userId}")
    public ResponseEntity<FriendRequest> sendRequest(@PathVariable Long userId) {
        Long senderId = getCurrentUserId();
        FriendRequest request = friendRequestService.sendRequest(senderId, userId);
        return ResponseEntity.ok(request);
    }



    @PostMapping("/accept/{friendId}")
    public ResponseEntity<Void> acceptRequest(@PathVariable Long friendId) {
        Long receiverId = getCurrentUserId();
        friendRequestService.acceptRequest(friendId, receiverId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete/{friendId}")
    public ResponseEntity<Void> deleteFriendship(@PathVariable Long friendId) {
        Long currentUserId = getCurrentUserId();
        friendRequestService.deleteFriendship(currentUserId, friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all-requests")
    public ResponseEntity<List<FriendRequestWithUserRequest>> getAllRequests() {
        Long receiverId = getCurrentUserId();
        List<FriendRequestWithUserRequest> requests = friendRequestService.getAllPendingRequestsWithNames(receiverId);
        return ResponseEntity.ok(requests);
    }


    @GetMapping("/all")
    public ResponseEntity<List<FriendsRequest>> getAllFriends() {
        Long currentUserId = getCurrentUserId();
        List<FriendsRequest> friends = friendRequestService.getAllFriends(currentUserId);
        return ResponseEntity.ok(friends);
    }

    private Long getCurrentUserId() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));
        AuthEntity user = authPsqlRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found by email"));
        return user.getId();
    }
}
