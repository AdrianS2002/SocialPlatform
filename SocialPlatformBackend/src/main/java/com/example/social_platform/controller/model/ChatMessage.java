package com.example.social_platform.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;


@Data
@AllArgsConstructor
public class ChatMessage {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Instant sentAt;
}
