package com.example.social_platform.persistance.model;


import com.example.social_platform.controller.model.AlbumResponse;
import com.example.social_platform.service.model.Album;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    Album fromEntity(AlbumEntity albumEntity);
    AlbumEntity fromDomain(Album album);
    List<Album> fromEntities(List<AlbumEntity> albumEntities);
    AlbumResponse toResponse(Album album);
    List<AlbumResponse> toResponses(List<Album> albums);
}
