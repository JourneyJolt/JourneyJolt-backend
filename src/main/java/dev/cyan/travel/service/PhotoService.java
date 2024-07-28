package dev.cyan.travel.service;

import dev.cyan.travel.entity.Photo;
import dev.cyan.travel.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    public Photo getById(String id) {
        return photoRepository.findById(id).orElseThrow();
    }

    public String add(MultipartFile file) throws IOException {
        Photo saved = photoRepository.save(Photo.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(new Binary(BsonBinarySubType.BINARY, file.getBytes()))
                .build());

        return saved.getId();
    }

    public void delete(String photoId) {
        photoRepository.deleteById(photoId);
    }
}
