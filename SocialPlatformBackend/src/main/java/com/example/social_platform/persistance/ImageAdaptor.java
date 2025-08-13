package com.example.social_platform.persistance;

import com.example.social_platform.persistance.model.ImageEntity;
import com.example.social_platform.persistance.repository.AlbumPsqlRepository;
import com.example.social_platform.persistance.repository.ImagePsqlRepository;
import com.example.social_platform.service.model.Image;
import com.example.social_platform.service.ports.outgoing.ImageRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.social_platform.constants.AuthoritiesConstants.UPLOAD_DIR;

@Service
public class ImageAdaptor implements ImageRepository {

    private final ImagePsqlRepository imagePsqlRepository;
    private final AlbumPsqlRepository albumPsqlRepositoory;

    public ImageAdaptor(ImagePsqlRepository imagePsqlRepository, AlbumPsqlRepository albumPsqlRepositoory) {
        this.imagePsqlRepository = imagePsqlRepository;
        this.albumPsqlRepositoory = albumPsqlRepositoory;
    }

    @Override
    public void save(Image image) throws IOException {

        final var albumEntity = albumPsqlRepositoory.getReferenceById(image.getAlbumId());
        final var filename = UUID.randomUUID() + "_" + image.getFile().getOriginalFilename();
        Path path= Paths.get(UPLOAD_DIR + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getFile().getBytes());

        ImageEntity imageEntity = ImageEntity.builder()
                .filename(filename)
                .path(path.toString())
                .uploadedAt(LocalDateTime.now())
                .album(albumEntity)
                .isBlocked(Boolean.FALSE)
                .build();
        imagePsqlRepository.save(imageEntity);
    }

    @Override
    public void deletePhoto(Long photoId) {
        final var image = imagePsqlRepository.getReferenceById(photoId);

        Path path = Paths.get(UPLOAD_DIR + image.getFilename());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + path, e);
        }

        imagePsqlRepository.delete(image);

    }

    @Override
    public void blockPhoto(Long photoId) {
        final var image = imagePsqlRepository.getReferenceById(photoId);
        image.setIsBlocked(Boolean.TRUE);
        imagePsqlRepository.save(image);
    }

    @Override
    public void unblockPhoto(Long photoId) {
        final var image = imagePsqlRepository.getReferenceById(photoId);
        image.setIsBlocked(Boolean.FALSE);
        imagePsqlRepository.save(image);
    }
}
