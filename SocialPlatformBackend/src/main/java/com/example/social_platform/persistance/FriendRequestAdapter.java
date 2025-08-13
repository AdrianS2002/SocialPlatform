package com.example.social_platform.persistance;

import com.example.social_platform.persistance.model.FriendRequestEntity;
import com.example.social_platform.persistance.model.FriendRequestMapper;
import com.example.social_platform.persistance.repository.FriendRequestPsqlRepository;
import com.example.social_platform.service.model.FriendRequest;
import com.example.social_platform.service.ports.outgoing.FriendRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestAdapter implements FriendRequestRepository {

    private final FriendRequestPsqlRepository friendRequestRepository;
    private final FriendRequestMapper friendRequestMapper;

    public FriendRequestAdapter(FriendRequestPsqlRepository friendRequestRepository, FriendRequestMapper friendRequestMapper) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendRequestMapper = friendRequestMapper;
    }

    @Override
    public FriendRequest save(FriendRequest friendRequest) {
        FriendRequestEntity entity = friendRequestMapper.fromDomain(friendRequest);
        FriendRequestEntity saved = friendRequestRepository.save(entity);
        return friendRequestMapper.fromEntity(saved);
    }

    @Override
    public Optional<FriendRequest> findBySenderAndReceiver(Long senderId, Long receiverId) {
        return friendRequestRepository.findBySenderIdAndReceivedId(senderId, receiverId)
                .map(friendRequestMapper::fromEntity);
    }

    @Override
    public List<FriendRequest> findAllByReceiverAndStatus(Long receiverId, FriendRequestEntity.Status status) {
        return friendRequestRepository.findAllByReceivedIdAndStatus(receiverId, status)
                .stream()
                .map(friendRequestMapper::fromEntity)
                .toList();
    }

    @Override
    public List<FriendRequest> findAllAcceptedFriends(Long userId) {
        List<FriendRequestEntity> all = friendRequestRepository
                .findAllBySenderIdOrReceivedId(userId, userId)
                .stream()
                .filter(req -> req.getStatus() == FriendRequestEntity.Status.ACCEPTED)
                .toList();

        return all.stream()
                .map(friendRequestMapper::fromEntity)
                .toList();
    }


}
