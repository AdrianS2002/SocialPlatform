package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.controller.model.FeedPostResponse;
import com.example.social_platform.persistance.model.AlbumEntity;
import com.example.social_platform.persistance.repository.AlbumPsqlRepository;
import com.example.social_platform.persistance.repository.ImagePsqlRepository;
import com.example.social_platform.persistance.repository.UserPsqlRepository;
import com.example.social_platform.service.ports.outgoing.FriendRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.social_platform.constants.AuthoritiesConstants.UPLOAD_DIR;

@Service
@Transactional
public class FeedFacade implements FeedService{

    private final AlbumPsqlRepository albumRepo;
    private final UserPsqlRepository userRepo;
    private final FriendRequestRepository friendRepo;
    private final ImagePsqlRepository imageRepo;

    public FeedFacade(AlbumPsqlRepository albumRepo,
                      UserPsqlRepository userRepo,
                      FriendRequestRepository friendRepo, ImagePsqlRepository imageRepo) {
        this.albumRepo = albumRepo;
        this.userRepo = userRepo;
        this.friendRepo = friendRepo;
        this.imageRepo = imageRepo;
    }

    @Override
    public List<FeedPostResponse> getNews(Long currentUserId) {
        List<AlbumEntity> allAlbums = albumRepo.findAll();

        var friendIds = friendRepo.findAllAcceptedFriends(currentUserId).stream()
                .map(f -> f.getSenderId().equals(currentUserId) ? f.getReceivedId() : f.getSenderId())
                .collect(Collectors.toSet());

        return allAlbums.stream()
                .map(album -> {
                    Long userId = album.getUser().getId();

                    var userOpt = userRepo.findById(userId);
                    if (userOpt.isEmpty()) return null;

                    var user = userOpt.get();
                    var images = imageRepo.findByAlbumIdOrderByUploadedAtAsc(album.getId());
                    if (images.isEmpty()) return null;

                    var firstImage = images.get(0);
                    var postedAt = firstImage.getUploadedAt();

                    String base64Image = null;
                    try {
                        Path path = Paths.get(UPLOAD_DIR + firstImage.getFilename());
                        if (Files.exists(path)) {
                            byte[] fileBytes = Files.readAllBytes(path);
                            base64Image = Base64.getEncoder().encodeToString(fileBytes);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return FeedPostResponse.builder()
                            .albumTitle(album.getTitle())
                            .albumDescription(album.getDescription())
                            .postedAt(postedAt)
                            .postedByName(user.getFirstName() + " " + user.getLastName())
                            .postedById(user.getId())
                            .isFriend(friendIds.contains(user.getId()))
                            .base64Image(base64Image)
                            .imageId(firstImage.getId())
                            .isBlocked(firstImage.getIsBlocked())
                            .build();
                })
                .filter(Objects::nonNull)
                .sorted((a, b) -> {
                    boolean aIsFriend = friendIds.contains(a.getPostedById());
                    boolean bIsFriend = friendIds.contains(b.getPostedById());
                    if (aIsFriend != bIsFriend) return aIsFriend ? -1 : 1;
                    return b.getPostedAt().compareTo(a.getPostedAt());
                })
                .toList();
    }



}
