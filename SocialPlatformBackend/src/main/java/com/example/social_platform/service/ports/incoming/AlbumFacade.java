package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.persistance.model.ImageEntity;
import com.example.social_platform.persistance.repository.AuthPsqlRepository;
import com.example.social_platform.persistance.repository.ImagePsqlRepository;
import com.example.social_platform.service.model.Album;
import com.example.social_platform.service.ports.outgoing.AlbumRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AlbumFacade implements AlbumService{

    private final AlbumRepository albumRepository;
    private final ImagePsqlRepository imagePsqlRepository;
    private final AuthPsqlRepository authRepository;

    public AlbumFacade(AlbumRepository albumRepository, ImagePsqlRepository imagePsqlRepository, AuthPsqlRepository authRepository) {
        this.albumRepository = albumRepository;
        this.imagePsqlRepository = imagePsqlRepository;
        this.authRepository = authRepository;
    }

    @Override
    @CacheEvict(value = "albums_by_user", key = "#album.userId")
    public Album create(Album album) {
        return albumRepository.create(album);
    }

    @Override
    @Cacheable(value = "photos_by_album", key = "#albumId")
    public List<ImageEntity> getPhotoAlbum(Long albumId) {
        return imagePsqlRepository.findByAlbumIdAndIsBlockedFalse(albumId);
    }

    @Override
    @Cacheable(value = "albums_by_user", key = "#userID")
    public List<Album> getAlbumsUser(Long userID) {
        return albumRepository.getAllAlbums(userID);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "albums_by_user", allEntries = true),
            @CacheEvict(value = "photos_by_album", key = "#albumId")
    })
    public void deleteAlbum(Long albumId) {
        albumRepository.deleteAlbum(albumId);
    }

    @Override
    @CacheEvict(value = "albums_by_user", key = "#album.userId", allEntries = true)
    public Album updateAlbum(Album album) {
        return albumRepository.editAlbum(album);
    }

    @Override
    public String getUserEmailByImageId(Long imageId) {
        var image = imagePsqlRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found (ID = " + imageId + ")"));

        var album = image.getAlbum();
        if (album == null || album.getUser() == null) {
            throw new RuntimeException("Album or user not found for image ID = " + imageId);
        }

        var auth = authRepository.findById(album.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Auth not found for user ID = " + album.getUser().getId()));

        return auth.getEmail();
    }


}