package com.example.social_platform.controller;

import com.example.social_platform.service.model.Image;
import com.example.social_platform.service.ports.incoming.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImage(@RequestParam MultipartFile file,
                            @RequestParam Long albumId) throws IOException {
        imageService.uploadImage(Image.builder()

                .file(file)
                .albumId(albumId)
                .build());

    }

    @DeleteMapping("/delete-photo")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePhoto(@RequestParam Long id) {
        imageService.deletePhoto(id);
    }

    @PostMapping("/block-photo")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void blockPhoto(@RequestParam Long id) {
        imageService.blockPhoto(id);
    }

    @PostMapping("/unblock-photo")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void unblockPhoto(@RequestParam Long id) {
        imageService.unblockPhoto(id);
    }

}