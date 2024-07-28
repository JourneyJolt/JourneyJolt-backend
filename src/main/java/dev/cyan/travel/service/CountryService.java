package dev.cyan.travel.service;

import dev.cyan.travel.DTO.CountryDTO;
import dev.cyan.travel.entity.City;
import dev.cyan.travel.entity.Country;
import dev.cyan.travel.mapper.CountryMapper;
import dev.cyan.travel.repository.CityRepository;
import dev.cyan.travel.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final CityService cityService;
    private final CountryMapper countryMapper;

    public List<CountryDTO> getAllEnabled() {
        return countryRepository
                .findCountriesByEnabledIsTrueOrderByName()
                .stream()
                .map(countryMapper::toDTO)
                .toList();
    }

    public List<CountryDTO> getAll() {
        return countryRepository
                .findAllByOrderByName()
                .stream()
                .map(countryMapper::toDTO)
                .toList();
    }

    public Optional<CountryDTO> getById(String id) {
        return countryRepository
                .findById(id)
                .map(countryMapper::toDTO);
    }

    public CountryDTO create(CountryDTO countryDTO) {
        Country country = countryMapper.fromDTO(countryDTO);
        country.setEnabled(true);
        Country createdCountry = countryRepository.save(country);
        return countryMapper.toDTO(createdCountry);
    }

    public CountryDTO update(String id, CountryDTO countryDTO) {
        Country country = countryRepository
                .findById(id)
                .orElseThrow();
        countryMapper.updateCountry(country, countryDTO);
        Country modifiedCountry = countryRepository.save(country);
        return countryMapper.toDTO(modifiedCountry);
    }

    public void disable(String id) {
        Country country = countryRepository.findById(id).orElseThrow();
        country.setEnabled(false);

        List<City> cities = cityRepository.findCitiesByCountryOrderByName(country);
        for (City city : cities) {
            cityService.disable(city.getId());
        }

        countryRepository.save(country);
    }

    public void enable(String id) {
        Country country = countryRepository.findById(id).orElseThrow();
        country.setEnabled(true);
        countryRepository.save(country);
    }

    public void delete(String id) {
        Country country = countryRepository
                .findById(id)
                .orElseThrow();
        List<City> cities = cityRepository.findCitiesByCountryOrderByName(country);

        for (City city : cities) {
            cityService.delete(city.getId());
        }

        countryRepository.deleteById(id);
    }
}
