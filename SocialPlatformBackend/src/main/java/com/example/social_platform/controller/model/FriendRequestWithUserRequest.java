package com.example.social_platform.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendRequestWithUserRequest {
    private Long id;
    private Long senderId;
    private Long receivedId;
    private String status;
    private String firstName;
    private String lastName;
}
