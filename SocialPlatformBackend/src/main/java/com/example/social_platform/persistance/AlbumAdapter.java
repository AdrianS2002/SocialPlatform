package com.example.social_platform.persistance;

import com.example.social_platform.config.SecurityUtils;
import com.example.social_platform.controller.AlbumController;
import com.example.social_platform.persistance.model.AlbumEntity;
import com.example.social_platform.persistance.model.AlbumMapper;
import com.example.social_platform.persistance.model.ImageEntity;
import com.example.social_platform.persistance.repository.AlbumPsqlRepository;
import com.example.social_platform.persistance.repository.ImagePsqlRepository;
import com.example.social_platform.persistance.repository.UserPsqlRepository;
import com.example.social_platform.service.model.Album;
import com.example.social_platform.service.ports.outgoing.AlbumRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.social_platform.constants.AuthoritiesConstants.UPLOAD_DIR;

@Service
public class AlbumAdapter implements AlbumRepository {

    private final AlbumMapper albumMapper;
    private final AlbumPsqlRepository albumPsqlRepositoory;
    private final UserPsqlRepository userPsqlRepository;
    private final ImagePsqlRepository imagePsqlRepository;

    public AlbumAdapter(AlbumMapper albumMapper,
                        AlbumPsqlRepository albumPsqlRepositoory,
                        UserPsqlRepository userPsqlRepository, ImagePsqlRepository imagePsqlRepository) {
        this.albumMapper = albumMapper;
        this.albumPsqlRepositoory = albumPsqlRepositoory;
        this.userPsqlRepository = userPsqlRepository;
        this.imagePsqlRepository = imagePsqlRepository;
    }

    @Override
    public Album create(Album album) {
        final var userEntity = userPsqlRepository.getReferenceById(album.getUserId());
        final var albumEntity = AlbumEntity.builder()
                .title(album.getTitle())
                .description(album.getDescription())
                .user(userEntity)
                .build();
        return albumMapper.fromEntity(albumPsqlRepositoory.save(albumEntity));
    }

    @Override
    public List<Album> getAllAlbums(Long userId) {
        return albumMapper.fromEntities(albumPsqlRepositoory.findAlbumsByUserId(userId));
    }

    @Override
    public void deleteAlbum(Long albumId) {
        List<ImageEntity> images = imagePsqlRepository.findByAlbumId(albumId);

        for (ImageEntity image : images) {
            Path path = Paths.get(UPLOAD_DIR + image.getFilename());
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file: " + image.getFilename(), e);
            }
        }
        albumPsqlRepositoory.deleteById(albumId);
    }

    @Override
    public Album editAlbum(Album album) {

        final var actualAlbum = albumPsqlRepositoory.getReferenceById(album.getId());
        final var editedAlbum = AlbumEntity.builder()
                .id(actualAlbum.getId())
                .user(actualAlbum.getUser())
                .title(album.getTitle())
                .description(album.getDescription())
                .build();
        return albumMapper.fromEntity(albumPsqlRepositoory.save(editedAlbum));
    }
}
