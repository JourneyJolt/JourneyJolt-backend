package dev.cyan.travel.controller;

import dev.cyan.travel.DTO.BookingDTO;
import dev.cyan.travel.DTO.HotelDTO;
import dev.cyan.travel.service.BookingService;
import dev.cyan.travel.service.HotelService;
import dev.cyan.travel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final BookingService bookingService;
    private final RoomService roomService;
    private final HotelService hotelService;

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<BookingDTO>> searchForBookings(@RequestParam(value = "userId", required = false) String userId,
                                                              @RequestParam(value = "hotelId", required = false) String hotelId) {
        if (userId != null && hotelId != null) {
            return ResponseEntity.ok(getBookingsList(hotelId, bookingService.getBookingsByUserId(userId)));
        } else if (userId != null) {
            ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
        } else if (hotelId != null) {
            return ResponseEntity.ok(getBookingsList(hotelId, bookingService.getAll()));
        }

        return ResponseEntity.ok(bookingService.getAll());
    }

    private List<BookingDTO> getBookingsList(String hotelId, List<BookingDTO> list) {
        List<BookingDTO> toReturn = new ArrayList<>();
        for (BookingDTO bookingDTO : list) {
            String tempId = roomService.getById(bookingDTO.getRoomId()).orElseThrow().getHotelId();
            if (tempId.equals(hotelId)) {
                toReturn.add(bookingDTO);
            }
        }
        return toReturn;
    }

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> searchForHotelsWithAvailableRooms(
            @RequestParam(value = "countryId", required = false) String countryId,
            @RequestParam(value = "cityId", required = false) String cityId,
            @RequestParam(value = "bookedSince", required = false) String bookedSince,
            @RequestParam(value = "bookedTo", required = false) String bookedTo,
            @RequestParam(value = "capacity", required = false) Integer capacity) {
        if (bookedSince != null && bookedTo != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            return ResponseEntity.ok(
                    hotelService.getHotelsWithAvailableRooms(
                            countryId,
                            cityId,
                            LocalDate.parse(bookedSince, formatter),
                            LocalDate.parse(bookedTo, formatter),
                            capacity));
        }
        if (cityId != null) {
            return ResponseEntity.ok(hotelService.getEnabledHotelsByCityId(cityId));
        }
        if (countryId != null) {
            return ResponseEntity.ok(hotelService.getEnabledHotelsByCountryId(countryId));
        }

        return ResponseEntity.ok(hotelService.getAllEnabled());
    }
}
