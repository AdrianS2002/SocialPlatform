package com.example.social_platform.persistance.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "friend_request", schema = "public")
public class FriendRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long senderId;
    private long receivedId;
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {PENDING, ACCEPTED, DECLINED}

}
