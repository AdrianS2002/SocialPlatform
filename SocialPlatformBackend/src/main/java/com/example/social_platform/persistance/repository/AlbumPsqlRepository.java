package com.example.social_platform.persistance.repository;

import com.example.social_platform.persistance.model.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumPsqlRepository extends JpaRepository<AlbumEntity, Long> {

    @Query("SELECT a FROM AlbumEntity a WHERE a.user.id = :userId")
    List<AlbumEntity> findAlbumsByUserId(@Param("userId") Long userId);

}
