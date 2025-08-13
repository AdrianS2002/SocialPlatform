package com.example.social_platform.service.model;

import com.example.social_platform.persistance.model.FriendRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {
    private Long id;
    private Long senderId;
    private Long receivedId;
    private FriendRequestEntity.Status status;
}
