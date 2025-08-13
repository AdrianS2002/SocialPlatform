package com.example.social_platform.persistance.model;


import com.example.social_platform.service.model.Auth;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    Auth fromEntity(AuthEntity userEntity);

    AuthEntity fromDomain(Auth user);
}
