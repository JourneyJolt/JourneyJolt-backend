package dev.cyan.travel.repository;

import dev.cyan.travel.entity.City;
import dev.cyan.travel.entity.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends MongoRepository<City, String> {
    List<City> findAllByOrderByName();

    City getById(String id);

    List<City> findCitiesByCountryOrderByName(Country country);

    List<City> findCitiesByEnabledIsTrueAndCountryOrderByName(Country country);

    List<City> findCitiesByEnabledIsTrueOrderByName();
}
