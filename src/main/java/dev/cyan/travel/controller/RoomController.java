package dev.cyan.travel.controller;

import dev.cyan.travel.DTO.BookingDTO;
import dev.cyan.travel.DTO.RoomDTO;
import dev.cyan.travel.service.BookingService;
import dev.cyan.travel.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllEnabled() {
        return ResponseEntity.ok(roomService.getAllEnabled());
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoomDTO>> getAll() {
        return ResponseEntity.ok(roomService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getById(@PathVariable String id) {
        return ResponseEntity.of(roomService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<RoomDTO> create(@Valid @RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.create(roomDTO));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<RoomDTO> update(@PathVariable String id, @Valid @RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.update(id, roomDTO));
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> disable(@PathVariable String id){
        roomService.disable(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> enable(@PathVariable String id){
        roomService.enable(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id){
        roomService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/bookings")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<BookingDTO>> getBookingsByRoom(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.getBookingsByRoomId(id));
    }
}
