package dev.cyan.travel.service;

import dev.cyan.travel.DTO.BookingDTO;
import dev.cyan.travel.DTO.UserDTO;
import dev.cyan.travel.entity.Booking;
import dev.cyan.travel.entity.User;
import dev.cyan.travel.enums.EBookingState;
import dev.cyan.travel.mapper.UserMapper;
import dev.cyan.travel.repository.BookingRepository;
import dev.cyan.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    public List<UserDTO> getAll() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public Optional<UserDTO> getById(String id) {
        return userRepository
                .findById(id)
                .map(userMapper::toDTO);
    }

    public UserDTO updateRoles(String id, UserDTO userDTO) {
        User user = userRepository
                .findById(id)
                .orElseThrow();
        user.setRoles(userMapper.fromDTO(userDTO).getRoles());
        User modifiedUser = userRepository.save(user);
        return userMapper.toDTO(modifiedUser);
    }

    public void disable(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(false);

        List<Booking> bookings = bookingRepository.findBookingsByUser(user);
        for (Booking booking : bookings) {
            bookingService.updateState(booking.getId(), new BookingDTO(EBookingState.CANCELED.toString()));
        }

        userRepository.save(user);
    }

    public void enable(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(true);
        userRepository.save(user);
    }

    public Optional<UserDTO> getByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(userMapper::toDTO);
    }

//    public void delete(String id) {
//        userRepository.deleteById(id);
//    }
}
