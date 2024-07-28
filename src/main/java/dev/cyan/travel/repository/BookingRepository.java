package dev.cyan.travel.repository;

import dev.cyan.travel.entity.Booking;
import dev.cyan.travel.entity.Room;
import dev.cyan.travel.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findBookingsByUser(User user);

    List<Booking> findBookingsByRoom(Room room);
}
