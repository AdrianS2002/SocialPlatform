package com.example.social_platform.service.ports.outgoing;

import com.example.social_platform.persistance.model.FriendRequestEntity;
import com.example.social_platform.service.model.FriendRequest;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository {
    @Transactional
    FriendRequest save(FriendRequest request);

    @Transactional
    Optional<FriendRequest> findBySenderAndReceiver(Long senderId, Long receiverId);

    @Transactional
    List<FriendRequest> findAllByReceiverAndStatus(Long receiverId, FriendRequestEntity.Status status);

    @Transactional
    List<FriendRequest> findAllAcceptedFriends(Long userId);

}
