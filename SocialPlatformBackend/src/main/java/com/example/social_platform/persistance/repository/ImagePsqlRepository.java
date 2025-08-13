package com.example.social_platform.persistance.repository;

import com.example.social_platform.persistance.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagePsqlRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByAlbumId(Long albumId);
    List<ImageEntity> findByAlbumIdOrderByUploadedAtAsc(Long albumId);
    void deleteById(Long id);
    List<ImageEntity> findByAlbumIdAndIsBlockedFalse(Long albumId);


}

