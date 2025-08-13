package com.example.social_platform.persistance.repository;

import com.example.social_platform.persistance.model.FriendRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestPsqlRepository extends JpaRepository<FriendRequestEntity, Long> {
    Optional<FriendRequestEntity> findBySenderIdAndReceivedId(Long senderId, Long receivedId);
    List<FriendRequestEntity> findAllByReceivedIdAndStatus(Long receivedId, FriendRequestEntity.Status status);
    List<FriendRequestEntity> findAllBySenderIdOrReceivedId(Long senderId, Long receivedId);

}