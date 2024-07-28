package dev.cyan.travel.controller;

import dev.cyan.travel.DTO.CityDTO;
import dev.cyan.travel.DTO.CountryDTO;
import dev.cyan.travel.DTO.HotelDTO;
import dev.cyan.travel.service.CityService;
import dev.cyan.travel.service.CountryService;
import dev.cyan.travel.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryController {
    private final CountryService countryService;
    private final HotelService hotelService;
    private final CityService cityService;

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllEnabled() {
        return ResponseEntity.ok(countryService.getAllEnabled());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CountryDTO>> getAll() {
        return ResponseEntity.ok(countryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> getById(@PathVariable String id) {
        return ResponseEntity.of(countryService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CountryDTO> create(@Valid @RequestBody CountryDTO countryDTO) {
        return ResponseEntity.ok(countryService.create(countryDTO));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CountryDTO> update(@PathVariable String id, @Valid @RequestBody CountryDTO countryDTO) {
        return ResponseEntity.ok(countryService.update(id, countryDTO));
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> disable(@PathVariable String id) {
        countryService.disable(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> enable(@PathVariable String id) {
        countryService.enable(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        countryService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/hotels")
    public ResponseEntity<List<HotelDTO>> getEnabledHotelsInCountry(@PathVariable String id) {
        return ResponseEntity.ok(hotelService.getEnabledHotelsByCountryId(id));
    }

    @GetMapping("/all/{id}/hotels")
    public ResponseEntity<List<HotelDTO>> getHotelsInCountry(@PathVariable String id) {
        return ResponseEntity.ok(hotelService.getHotelsByCountryId(id));
    }

    @GetMapping("/{id}/cities")
    public ResponseEntity<List<CityDTO>> getEnabledCitiesInCountry(@PathVariable String id) {
        return ResponseEntity.ok(cityService.getEnabledCitiesByCountryId(id));
    }

    @GetMapping("/all/{id}/cities")
    public ResponseEntity<List<CityDTO>> getCitiesByCountryId(@PathVariable String id) {
        return ResponseEntity.ok(cityService.getCitiesByCountryId(id));
    }
}
