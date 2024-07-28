package dev.cyan.travel.controller;

import dev.cyan.travel.DTO.HotelDTO;
import dev.cyan.travel.DTO.RoomDTO;
import dev.cyan.travel.response.MessageResponse;
import dev.cyan.travel.service.HotelService;
import dev.cyan.travel.service.PhotoService;
import dev.cyan.travel.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {
    private final HotelService hotelService;
    private final RoomService roomService;
    private final PhotoService photoService;

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllEnabled() {
        return ResponseEntity.ok(hotelService.getAllEnabled());
    }

    @GetMapping("/all")
    public ResponseEntity<List<HotelDTO>> getAll() {
        return ResponseEntity.ok(hotelService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getById(@PathVariable String id) {
        return ResponseEntity.of(hotelService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> create(@Valid @RequestBody HotelDTO hotelDTO) {
        Optional<HotelDTO> optionalHotelDTO = hotelService.create(hotelDTO);
        if (optionalHotelDTO.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("City in this country doesn't exist"));
        }

        return ResponseEntity.ok(optionalHotelDTO.get());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody HotelDTO hotelDTO) {
        Optional<HotelDTO> optionalHotelDTO = hotelService.update(id, hotelDTO);
        if (optionalHotelDTO.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("City in this country doesn't exist"));
        }

        return ResponseEntity.ok(optionalHotelDTO.get());
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> disable(@PathVariable String id) {
        hotelService.disable(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> enable(@PathVariable String id) {
        hotelService.enable(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        hotelService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<RoomDTO>> getEnabledRoomsInHotel(@PathVariable String id,
                                                                @RequestParam(value = "bookedSince", required = false) String bookedSince,
                                                                @RequestParam(value = "bookedTo", required = false) String bookedTo,
                                                                @RequestParam(value = "capacity", required = false) Integer capacity) {
        if (bookedSince != null && bookedTo != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            return ResponseEntity.ok(
                    roomService.getAllAvailableRoomsForPeriod(id,
                            LocalDate.parse(bookedSince, formatter),
                            LocalDate.parse(bookedTo, formatter),
                            capacity));
        }

        return ResponseEntity.ok(roomService.getEnabledRoomsByHotelId(id));
    }

    @GetMapping("/all/{id}/rooms")
    public ResponseEntity<List<RoomDTO>> getRoomsInHotel(@PathVariable String id) {
        return ResponseEntity.ok(roomService.getRoomsByHotelId(id));
    }

    @PostMapping("/{id}/photos")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<MessageResponse> addPhotosOfHotel(@PathVariable String id,
                                                            @RequestParam("photos") MultipartFile[] files) throws IOException {
        HotelDTO hotelDTO = hotelService.getById(id).orElseThrow();

        Set<String> photosIds = hotelDTO.getPhotosIds();
        for (MultipartFile file : files) {
            photosIds.add(photoService.add(file));
        }

        hotelDTO.setPhotosIds(photosIds);
        hotelService.update(id, hotelDTO);

        return ResponseEntity.ok(new MessageResponse("Photo uploaded successfully!"));
    }

    @DeleteMapping("/{id}/photos")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deletePhotoOfHotel(@PathVariable String id, @RequestParam("photoId") String photoId) {
        HotelDTO hotelDTO = hotelService.getById(id).orElseThrow();

        Set<String> photosIds = hotelDTO.getPhotosIds();
        photosIds.removeIf(pId -> pId.equals(photoId));

        hotelDTO.setPhotosIds(photosIds);
        hotelService.update(id, hotelDTO);

        photoService.delete(photoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
