package com.example.social_platform.service.ports.outgoing;

import com.example.social_platform.service.model.Image;
import jakarta.transaction.Transactional;

import java.io.IOException;

public interface ImageRepository {

    @Transactional
    public void save(Image image) throws IOException;
    @Transactional
    public void deletePhoto(Long photoId);
    @Transactional
    public void blockPhoto(Long photoId);
    @Transactional
    public void unblockPhoto(Long photoId);
}
