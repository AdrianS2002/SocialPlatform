package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.controller.model.FriendRequestWithUserRequest;
import com.example.social_platform.controller.model.FriendsRequest;
import com.example.social_platform.service.model.FriendRequest;
import jakarta.transaction.Transactional;

import java.util.List;


public interface FriendRequestService {
    @Transactional
    FriendRequest sendRequest(Long senderId, Long receiverId);

    @Transactional
    void acceptRequest(Long senderId, Long receiverId);

    @Transactional
    void deleteFriendship(Long userId1, Long userId2);

    @Transactional
    List<FriendRequestWithUserRequest> getAllPendingRequestsWithNames(Long receiverId);


    @Transactional
    List<FriendsRequest> getAllFriends(Long userId);
}
