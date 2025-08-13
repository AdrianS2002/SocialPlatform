package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.persistance.model.ImageEntity;
import com.example.social_platform.service.model.Album;
import jakarta.transaction.Transactional;

import java.util.List;

public interface AlbumService {

    @Transactional
    Album create(Album album);

    @Transactional
    List<ImageEntity> getPhotoAlbum(Long albumId);

    @Transactional
    List<Album> getAlbumsUser(Long userID);

    @Transactional
    void deleteAlbum(Long albumId);

    @Transactional
    Album updateAlbum(Album album);

    @Transactional
    String getUserEmailByImageId(Long imageId);

}
