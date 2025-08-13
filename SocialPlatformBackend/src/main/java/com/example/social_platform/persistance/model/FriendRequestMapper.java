package com.example.social_platform.persistance.model;

import com.example.social_platform.service.model.FriendRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {
    @Mapping(source = "receivedId", target = "receivedId")
    FriendRequestEntity fromDomain(FriendRequest friendRequest);
    @Mapping(source = "receivedId", target = "receivedId")
    FriendRequest fromEntity(FriendRequestEntity friendRequestEntity);
}
