package dev.cyan.travel.controller;

import dev.cyan.travel.DTO.BookingDTO;
import dev.cyan.travel.DTO.RoomDTO;
import dev.cyan.travel.DTO.UserDTO;
import dev.cyan.travel.config.jwt.JwtUtils;
import dev.cyan.travel.enums.EBookingState;
import dev.cyan.travel.response.MessageResponse;
import dev.cyan.travel.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private final MailService mailService;
    private final RoomService roomService;
    private final HotelService hotelService;
    private final JwtUtils jwtUtils;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<BookingDTO>> getAll() {
        return ResponseEntity.ok(bookingService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<BookingDTO> getById(@PathVariable String id) {
        return ResponseEntity.of(bookingService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody BookingDTO bookingDTO) {
        RoomDTO roomDTO = roomService.getById(bookingDTO.getRoomId()).orElseThrow();
        long between = ChronoUnit.DAYS.between(bookingDTO.getBookedSince(), bookingDTO.getBookedTo());
        if (bookingDTO.getPrice() != (roomDTO.getPrice() * between)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Not correct price"));
        }

        Optional<BookingDTO> optionalBookingDTO = bookingService.create(bookingDTO);
        if (optionalBookingDTO.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Booking isn't available for those dates"));
        }

        return sendMessageAndGetResponse(optionalBookingDTO.get());
    }

    private ResponseEntity<BookingDTO> sendMessageAndGetResponse(BookingDTO bookingDTO) {
        String email = userService.getById(bookingDTO.getUserId()).get().getEmail();
        int roomNumber = roomService.getById(bookingDTO.getRoomId()).get().getRoomNumber();
        String hotelName = hotelService.getById(
                roomService.getById(bookingDTO.getRoomId()).get().getHotelId()).get().getName();

        mailService.send(email, "The status of your booking for room " + roomNumber + " in " + hotelName + " is - <b>"
                + bookingDTO.getState() + "</b>");
        return ResponseEntity.ok(bookingDTO);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody BookingDTO bookingDTO) {
        Optional<BookingDTO> optionalBookingDTO = bookingService.update(id, bookingDTO);
        if (optionalBookingDTO.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Booking isn't available for those dates"));
        }
        return ResponseEntity.ok(optionalBookingDTO.get());
    }

    @PatchMapping("/{id}/state")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> updateState(@PathVariable String id, @RequestBody BookingDTO bookingDTO) {
        Optional<BookingDTO> optionalBookingDTO = bookingService.updateState(id, bookingDTO);
        if (optionalBookingDTO.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse(
                    "Can't change state of this booking because exists another " +
                            "booking for this room with non CANCELED status"));
        }

        return sendMessageAndGetResponse(optionalBookingDTO.get());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        bookingService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> cancelBooking(@PathVariable String id, @RequestHeader("Authorization") String jwt) {
        BookingDTO byId = bookingService.getById(id).orElseThrow();

        String username = jwtUtils.getUserNameFromJwtToken(jwt.substring(7));
        UserDTO user = userService.getByUsername(username).orElseThrow();
        if (!user.getId().equals(byId.getUserId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Can't cancel another user's booking"));
        }

        Optional<BookingDTO> optionalBookingDTO = bookingService.updateState(id, new BookingDTO(EBookingState.CANCELED.toString()));
        if (optionalBookingDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return sendMessageAndGetResponse(optionalBookingDTO.get());
    }

    @GetMapping("/list/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getListForUser(@PathVariable String userId, @RequestHeader("Authorization") String jwt) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt.substring(7));
        UserDTO user = userService.getByUsername(username).orElseThrow();
        if (!user.getId().equals(userId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Can't view another user's bookings"));
        }

        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
    }
}
