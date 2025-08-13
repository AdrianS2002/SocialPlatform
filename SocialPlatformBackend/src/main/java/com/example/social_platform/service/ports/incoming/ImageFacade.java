package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.service.model.Image;
import com.example.social_platform.service.ports.outgoing.ImageRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
public class ImageFacade implements ImageService{

    private final ImageRepository imageRepository;

    public ImageFacade(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    @CacheEvict(value = "photos_by_album", key = "#image.albumId")
    public void uploadImage(Image image) throws IOException {

        imageRepository.save(image);
    }

    @Override
    @CacheEvict(value = "photos_by_album",  allEntries = true)

    public void deletePhoto(Long photoId) {
        imageRepository.deletePhoto(photoId);
    }

    @Override
    public void blockPhoto(Long photoId) {
        imageRepository.blockPhoto(photoId);
    }

    @Override
    public void unblockPhoto(Long photoId) {
        imageRepository.unblockPhoto(photoId);
    }
}