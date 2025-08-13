package com.example.social_platform.persistance.model;

import com.example.social_platform.service.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromEntity(UserEntity userEntity);
    UserEntity fromDomain(User user);
}
