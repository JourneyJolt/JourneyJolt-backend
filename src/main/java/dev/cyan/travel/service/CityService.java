package dev.cyan.travel.service;

import dev.cyan.travel.DTO.CityDTO;
import dev.cyan.travel.entity.City;
import dev.cyan.travel.entity.Country;
import dev.cyan.travel.entity.Hotel;
import dev.cyan.travel.mapper.CityMapper;
import dev.cyan.travel.repository.CityRepository;
import dev.cyan.travel.repository.CountryRepository;
import dev.cyan.travel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final CountryRepository countryRepository;
    private final HotelRepository hotelRepository;
    private final HotelService hotelService;

    public List<CityDTO> getAllEnabled() {
        return cityRepository
                .findCitiesByEnabledIsTrueOrderByName()
                .stream()
                .map(cityMapper::toDTO)
                .toList();
    }

    public List<CityDTO> getAll() {
        return cityRepository
                .findAllByOrderByName()
                .stream()
                .map(cityMapper::toDTO)
                .toList();
    }

    public Optional<CityDTO> getById(String id) {
        return cityRepository
                .findById(id)
                .map(cityMapper::toDTO);
    }

    public CityDTO create(CityDTO cityDTO) {
        countryRepository.findById(cityDTO.getCountryId()).orElseThrow();
        City city = cityMapper.fromDTO(cityDTO);
        city.setEnabled(true);
        City savedCity = cityRepository.save(city);
        return cityMapper.toDTO(savedCity);
    }

    public CityDTO update(String id, CityDTO cityDTO) {
        countryRepository.findById(cityDTO.getCountryId()).orElseThrow();
        City city = cityRepository.findById(id).orElseThrow();
        cityMapper.updateCity(city, cityDTO);
        City modifiedCity = cityRepository.save(city);
        return cityMapper.toDTO(modifiedCity);
    }

    public void disable(String id) {
        City city = cityRepository.findById(id).orElseThrow();
        city.setEnabled(false);

        List<Hotel> hotels = hotelRepository.findHotelsByCity(city);
        for (Hotel hotel : hotels) {
            hotelService.disable(hotel.getId());
        }

        cityRepository.save(city);
    }

    public void enable(String id) {
        City city = cityRepository.findById(id).orElseThrow();
        city.setEnabled(true);
        cityRepository.save(city);
    }

    public void delete(String id) {
        City city = cityRepository.findById(id).orElseThrow();
        List<Hotel> hotels = hotelRepository.findHotelsByCity(city);

        for (Hotel hotel : hotels) {
            hotelService.delete(hotel.getId());
        }

        cityRepository.deleteById(id);
    }

    public List<CityDTO> getEnabledCitiesByCountryId(String id) {
        Country country = countryRepository.findById(id).orElseThrow();
        List<City> citiesByCountry = cityRepository.findCitiesByEnabledIsTrueAndCountryOrderByName(country);
        return citiesByCountry
                .stream()
                .map(cityMapper::toDTO)
                .toList();
    }

    public List<CityDTO> getCitiesByCountryId(String id) {
        Country country = countryRepository.findById(id).orElseThrow();
        List<City> citiesByCountry = cityRepository.findCitiesByCountryOrderByName(country);
        return citiesByCountry
                .stream()
                .map(cityMapper::toDTO)
                .toList();
    }
}
