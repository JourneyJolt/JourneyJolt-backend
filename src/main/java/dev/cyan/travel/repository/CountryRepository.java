package dev.cyan.travel.repository;

import dev.cyan.travel.entity.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends MongoRepository<Country, String> {
    List<Country> findAllByOrderByName();

    Country getById(String id);

    List<Country> findCountriesByEnabledIsTrueOrderByName();
}
