package com.example.social_platform.service.ports.outgoing;

import com.example.social_platform.service.model.Album;
import com.example.social_platform.service.model.Image;
import jakarta.transaction.Transactional;

import java.util.List;


public interface AlbumRepository {

    @Transactional
    Album create(Album album);

    @Transactional
    List<Album> getAllAlbums(Long userId);

    @Transactional
    void deleteAlbum(Long albumId);

    @Transactional
    Album editAlbum(Album album);
}
