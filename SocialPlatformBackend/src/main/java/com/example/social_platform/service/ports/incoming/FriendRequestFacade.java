package com.example.social_platform.service.ports.incoming;


import com.example.social_platform.controller.model.FriendRequestWithUserRequest;
import com.example.social_platform.controller.model.FriendsRequest;
import com.example.social_platform.persistance.model.FriendRequestEntity;
import com.example.social_platform.persistance.repository.AuthPsqlRepository;
import com.example.social_platform.persistance.repository.UserPsqlRepository;
import com.example.social_platform.service.model.FriendRequest;
import com.example.social_platform.service.ports.outgoing.FriendRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class FriendRequestFacade implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final AuthPsqlRepository authPsqlRepository;
    private final UserPsqlRepository userPsqlRepository;

    public FriendRequestFacade(FriendRequestRepository friendRequestRepository,
                               AuthPsqlRepository authPsqlRepository,
                               UserPsqlRepository userPsqlRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.authPsqlRepository = authPsqlRepository;
        this.userPsqlRepository = userPsqlRepository;
    }



    @Override
    public FriendRequest sendRequest(Long senderId, Long receiverId) {
        Optional<FriendRequest> existing = friendRequestRepository.findBySenderAndReceiver(senderId, receiverId);
        if (existing.isPresent()) {
            throw new RuntimeException("Request already sent");
        }

        FriendRequest request = new FriendRequest();
        request.setSenderId(senderId);
        request.setReceivedId(receiverId);
        request.setStatus(FriendRequestEntity.Status.PENDING);
        return friendRequestRepository.save(request);
    }

    @Override
    public void acceptRequest(Long senderId, Long receiverId) {
        FriendRequest request = friendRequestRepository.findBySenderAndReceiver(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(FriendRequestEntity.Status.ACCEPTED);
        friendRequestRepository.save(request);
    }

    @Override
    public void deleteFriendship(Long userId1, Long userId2) {
        Optional<FriendRequest> request = friendRequestRepository.findBySenderAndReceiver(userId1, userId2);
        if (request.isEmpty()) {
            request = friendRequestRepository.findBySenderAndReceiver(userId2, userId1);
        }
        request.ifPresent(r -> friendRequestRepository.save(
                FriendRequest.builder()
                        .id(r.getId())
                        .senderId(r.getSenderId())
                        .receivedId(r.getReceivedId())
                        .status(FriendRequestEntity.Status.DECLINED)
                        .build()
        ));
    }

    @Override
    public List<FriendRequestWithUserRequest> getAllPendingRequestsWithNames(Long receiverId) {
        List<FriendRequest> requests = friendRequestRepository.findAllByReceiverAndStatus(receiverId, FriendRequestEntity.Status.PENDING);

        return requests.stream()
                .map(req -> {
                    var senderUser = userPsqlRepository.findById(req.getSenderId());
                    if (senderUser.isPresent()) {
                        var user = senderUser.get();
                        return new FriendRequestWithUserRequest(
                                req.getId(),
                                req.getSenderId(),
                                req.getReceivedId(),
                                req.getStatus().name(),
                                user.getFirstName(),
                                user.getLastName()
                        );
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }


    @Override
    public List<FriendsRequest> getAllFriends(Long userId) {
        List<FriendRequest> friends = friendRequestRepository.findAllAcceptedFriends(userId);

        return friends.stream()
                .map(req -> {
                    Long friendId = req.getSenderId().equals(userId) ? req.getReceivedId() : req.getSenderId();

                    var userOpt = userPsqlRepository.findById(friendId);
                    var authOpt = authPsqlRepository.findById(friendId);

                    if (userOpt.isPresent() && authOpt.isPresent()) {
                        var user = userOpt.get();
                        var auth = authOpt.get();
                        return new FriendsRequest(user.getFirstName() + " " + user.getLastName(), auth.getEmail(), auth.getId());
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }



}
