package dev.cyan.travel.service;

import dev.cyan.travel.DTO.BookingDTO;
import dev.cyan.travel.entity.Booking;
import dev.cyan.travel.entity.Room;
import dev.cyan.travel.entity.User;
import dev.cyan.travel.enums.EBookingState;
import dev.cyan.travel.mapper.BookingMapper;
import dev.cyan.travel.repository.BookingRepository;
import dev.cyan.travel.repository.BookingStateRepository;
import dev.cyan.travel.repository.RoomRepository;
import dev.cyan.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final BookingStateRepository bookingStateRepository;
    private final MongoTemplate mongoTemplate;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public List<BookingDTO> getAll() {
        return bookingRepository
                .findAll()
                .stream()
                .map(bookingMapper::toDTO)
                .toList();
    }

    public Optional<BookingDTO> getById(String id) {
        return bookingRepository
                .findById(id)
                .map(bookingMapper::toDTO);
    }

    public Optional<BookingDTO> create(BookingDTO bookingDTO) {
        Room room = roomRepository.findById(bookingDTO.getRoomId()).orElseThrow();
        User user = userRepository.findById(bookingDTO.getUserId()).orElseThrow();

        if (room.isEnabled() && user.isEnabled()) {
            Booking booking = bookingMapper.fromDTO(bookingDTO);
            if (checkIfBookingIsAvailable(booking.getRoom(), booking.getBookedSince(), booking.getBookedTo(), null)) {
                booking.setState(bookingStateRepository.getByName(EBookingState.PENDING));
                Booking createdBooking = bookingRepository.save(booking);
                return Optional.of(bookingMapper.toDTO(createdBooking));
            }
        }

        return Optional.empty();
    }

    public Boolean checkIfBookingIsAvailable(Room room, LocalDate bookedSince, LocalDate bookedTo, String id) {
        if (bookedSince == null || bookedTo == null || bookedSince.isAfter(bookedTo) ||
                bookedSince.isEqual(bookedTo)) {
            return false;
        }

        Query query = new Query().addCriteria(Criteria
                .where("room").is(room).andOperator(new Criteria().orOperator(
                        Criteria.where("bookedSince").gte(bookedSince).lte(bookedTo),
                        Criteria.where("bookedTo").gte(bookedSince).lte(bookedTo),
                        Criteria.where("bookedSince").lte(bookedSince).and("bookedTo").gte(bookedTo)
                )));
        List<Booking> bookings = mongoTemplate.find(query, Booking.class);

        List<Booking> filtered = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getState().getName() != EBookingState.CANCELED) {
                filtered.add(booking);
            }
        }

        if (id != null) {
            filtered.removeIf(booking -> id.equals(booking.getId()));
        }

        return filtered.isEmpty();
    }

    public Optional<BookingDTO> update(String id, BookingDTO bookingDTO) {
        roomRepository.findById(bookingDTO.getRoomId()).orElseThrow();
        userRepository.findById(bookingDTO.getUserId()).orElseThrow();
        Booking booking = bookingRepository.findById(id).orElseThrow();
        Booking fromDTO = bookingMapper.fromDTO(bookingDTO);
        if (checkIfBookingIsAvailable(fromDTO.getRoom(), fromDTO.getBookedSince(), fromDTO.getBookedTo(), id)) {
            bookingMapper.updateBooking(booking, bookingDTO);
            Booking modifiedBooking = bookingRepository.save(booking);
            return Optional.of(bookingMapper.toDTO(modifiedBooking));
        }

        return Optional.empty();
    }

    public Optional<BookingDTO> updateState(String id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id).orElseThrow();

        if (checkIfBookingIsAvailable(booking.getRoom(), booking.getBookedSince(), booking.getBookedTo(), id)) {
            String state = bookingDTO.getState();
            booking.setState(bookingStateRepository.getByName(Enum.valueOf(EBookingState.class, state)));
            Booking modifiedBooking = bookingRepository.save(booking);
            return Optional.of(bookingMapper.toDTO(modifiedBooking));
        }

        return Optional.empty();
    }

    public void delete(String id) {
        bookingRepository.deleteById(id);
    }

    public List<BookingDTO> getBookingsByUserId(String id) {
        User user = userRepository.findById(id).orElseThrow();
        List<Booking> bookingsByUser = bookingRepository
                .findBookingsByUser(user);
        return bookingsByUser
                .stream()
                .map(bookingMapper::toDTO)
                .toList();
    }

    public List<BookingDTO> getBookingsByRoomId(String id) {
        Room room = roomRepository.findById(id).orElseThrow();
        List<Booking> bookingsByRoom = bookingRepository
                .findBookingsByRoom(room);
        return bookingsByRoom
                .stream()
                .map(bookingMapper::toDTO)
                .toList();
    }
}
