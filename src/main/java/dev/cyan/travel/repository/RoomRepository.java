package dev.cyan.travel.repository;

import dev.cyan.travel.entity.Hotel;
import dev.cyan.travel.entity.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    Room getById(String id);

    List<Room> findRoomsByEnabledIsTrue();

    List<Room> findRoomsByHotel(Hotel hotel);

    List<Room> findRoomsByEnabledIsTrueAndHotel(Hotel hotel);
}
