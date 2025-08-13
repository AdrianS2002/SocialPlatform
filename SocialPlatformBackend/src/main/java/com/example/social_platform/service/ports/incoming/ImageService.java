package com.example.social_platform.service.ports.incoming;


import com.example.social_platform.service.model.Image;
import jakarta.transaction.Transactional;

import java.io.IOException;

public interface ImageService {

    @Transactional
    void uploadImage(Image image) throws IOException;

    @Transactional
    void deletePhoto(Long photoId);
    @Transactional
    void blockPhoto(Long photoId);
    @Transactional
    void unblockPhoto(Long photoId);

}
