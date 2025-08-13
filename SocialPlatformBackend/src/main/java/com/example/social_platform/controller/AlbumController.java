package com.example.social_platform.controller;


import com.example.social_platform.controller.model.AlbumRequest;
import com.example.social_platform.controller.model.AlbumResponse;
import com.example.social_platform.controller.model.ImageResponse;
import com.example.social_platform.controller.model.UpdateAlbumRequest;
import com.example.social_platform.persistance.model.AlbumMapper;
import com.example.social_platform.persistance.model.ImageEntity;
import com.example.social_platform.service.model.Album;
import com.example.social_platform.service.ports.incoming.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.example.social_platform.constants.AuthoritiesConstants.UPLOAD_DIR;

@CrossOrigin
@RestController
@RequestMapping("/album")
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumMapper albumMapper;

    public AlbumController(AlbumService albumService, AlbumMapper albumMapper) {
        this.albumService = albumService;
        this.albumMapper = albumMapper;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody AlbumRequest album) {
        albumService.create(Album.builder()
                .description(album.getDescription())
                .title(album.getTitle())
                .userId(album.getUserId())
                .build());
    }

    @GetMapping("/user-albums")
    public ResponseEntity<List<AlbumResponse>> getUserAlbums(@RequestParam Long userID) {
        return ResponseEntity.ok(albumMapper.toResponses(albumService.getAlbumsUser(userID)));
    }

    @GetMapping("/photos")
    public ResponseEntity<List<ImageResponse>> getImages(@RequestParam Long albumId) throws IOException {
        List<ImageEntity> images = albumService.getPhotoAlbum(albumId);

        List<ImageResponse> response = new ArrayList<>();

        for (ImageEntity image : images) {
            Path path = Paths.get(UPLOAD_DIR + image.getFilename());
            if (Files.exists(path)) {
                byte[] fileBytes = Files.readAllBytes(path);
                String base64 = Base64.getEncoder().encodeToString(fileBytes);
                response.add(ImageResponse.builder()
                        .id(image.getId())
                        .base64Content(base64)
                        .filename(image.getFilename())
                        .createdAt(image.getUploadedAt())
                        .build());
            }
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAlbum(@RequestParam Long albumId) {

        albumService.deleteAlbum(albumId);
    }

    @PostMapping("/edit")
    public ResponseEntity<AlbumResponse> editAlbum(@RequestBody UpdateAlbumRequest album) {

        Album editedAlbum = albumService.updateAlbum(Album.builder()
                .id(album.getId())
                .description(album.getDescription())
                .title(album.getTitle())
                .build());

        return ResponseEntity.ok(AlbumResponse.builder()
                .id(editedAlbum.getId())
                .description(editedAlbum.getDescription())
                .title(editedAlbum.getTitle())
                .build());
    }
}