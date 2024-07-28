package dev.cyan.travel.service;

import dev.cyan.travel.DTO.BookingDTO;
import dev.cyan.travel.DTO.RoomDTO;
import dev.cyan.travel.entity.Booking;
import dev.cyan.travel.entity.Hotel;
import dev.cyan.travel.entity.Room;
import dev.cyan.travel.enums.EBookingState;
import dev.cyan.travel.mapper.RoomMapper;
import dev.cyan.travel.repository.BookingRepository;
import dev.cyan.travel.repository.HotelRepository;
import dev.cyan.travel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    public List<RoomDTO> getAllEnabled() {
        return roomRepository
                .findRoomsByEnabledIsTrue()
                .stream()
                .map(roomMapper::toDTO)
                .toList();
    }

    public List<RoomDTO> getAll() {
        return roomRepository
                .findAll()
                .stream()
                .map(roomMapper::toDTO)
                .toList();
    }

    public Optional<RoomDTO> getById(String id) {
        return roomRepository
                .findById(id)
                .map(roomMapper::toDTO);
    }

    public RoomDTO create(RoomDTO roomDTO) {
        hotelRepository.findById(roomDTO.getHotelId()).orElseThrow();
        Room room = roomMapper.fromDTO(roomDTO);
        room.setEnabled(true);
        Room createdRoom = roomRepository.save(room);
        return roomMapper.toDTO(createdRoom);
    }

    public RoomDTO update(String id, RoomDTO roomDTO) {
        hotelRepository.findById(roomDTO.getHotelId()).orElseThrow();
        Room room = roomRepository
                .findById(id)
                .orElseThrow();
        roomMapper.updateRoom(room, roomDTO);
        Room modifiedRoom = roomRepository.save(room);
        return roomMapper.toDTO(modifiedRoom);
    }

    public void disable(String id) {
        Room room = roomRepository
                .findById(id)
                .orElseThrow();
        room.setEnabled(false);

        List<Booking> bookings = bookingRepository.findBookingsByRoom(room);
        for (Booking booking : bookings) {
            bookingService.updateState(booking.getId(), new BookingDTO(EBookingState.CANCELED.toString()));
        }

        roomRepository.save(room);
    }

    public void enable(String id) {
        Room room = roomRepository
                .findById(id)
                .orElseThrow();
        room.setEnabled(true);
        roomRepository.save(room);
    }

    public void delete(String id) {
        Room room = roomRepository
                .findById(id)
                .orElseThrow();

        List<Booking> bookings = bookingRepository.findBookingsByRoom(room);
        for (Booking booking : bookings) {
            bookingService.delete(booking.getId());
        }

        roomRepository.deleteById(id);
    }

    public List<RoomDTO> getEnabledRoomsByHotelId(String id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow();
        List<Room> roomsByHotel = roomRepository
                .findRoomsByEnabledIsTrueAndHotel(hotel);
        return roomsByHotel
                .stream()
                .map(roomMapper::toDTO)
                .toList();
    }

    public List<RoomDTO> getRoomsByHotelId(String id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow();
        List<Room> roomsByHotel = roomRepository
                .findRoomsByHotel(hotel);
        return roomsByHotel
                .stream()
                .map(roomMapper::toDTO)
                .toList();
    }

    public List<RoomDTO> getAllAvailableRoomsForPeriod(String hotelId, LocalDate bookedSince, LocalDate bookedTo, Integer capacity) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow();
        List<Room> roomsByHotel = roomRepository.findRoomsByEnabledIsTrueAndHotel(hotel);
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : roomsByHotel) {
            if (bookingService.checkIfBookingIsAvailable(room, bookedSince, bookedTo, null)) {
                if (capacity != null) {
                    if (room.getCapacity() == capacity) {
                        availableRooms.add(room);
                    }
                } else {
                    availableRooms.add(room);
                }
            }
        }

        return availableRooms
                .stream()
                .map(roomMapper::toDTO)
                .toList();
    }
}
