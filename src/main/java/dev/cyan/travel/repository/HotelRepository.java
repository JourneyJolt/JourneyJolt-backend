package dev.cyan.travel.repository;

import dev.cyan.travel.entity.City;
import dev.cyan.travel.entity.Country;
import dev.cyan.travel.entity.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, String> {
    Hotel getById(String id);

    List<Hotel> findHotelsByCountry(Country country);

    List<Hotel> findHotelsByCity(City city);

    List<Hotel> findHotelsByEnabledIsTrueAndCountry(Country country);

    List<Hotel> findHotelsByEnabledIsTrueAndCity(City city);

    List<Hotel> findHotelsByEnabledIsTrue();
}