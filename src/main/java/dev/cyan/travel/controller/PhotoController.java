package dev.cyan.travel.controller;

import dev.cyan.travel.entity.Photo;
import dev.cyan.travel.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Photo photo = photoService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(photo.getType()))
                .body(photo.getImage().getData());
    }
}
