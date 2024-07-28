package dev.cyan.travel.controller;

import dev.cyan.travel.DTO.CityDTO;
import dev.cyan.travel.DTO.HotelDTO;
import dev.cyan.travel.service.CityService;
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
@RequestMapping("/cities")
public class CityController {
    private final CityService cityService;
    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<CityDTO>> getAllEnabled() {
        return ResponseEntity.ok(cityService.getAllEnabled());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CityDTO>> getAll() {
        return ResponseEntity.ok(cityService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getById(@PathVariable String id) {
        return ResponseEntity.of(cityService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CityDTO> create(@Valid @RequestBody CityDTO cityDTO) {
        return ResponseEntity.ok(cityService.create(cityDTO));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CityDTO> update(@PathVariable String id, @Valid @RequestBody CityDTO cityDTO) {
        return ResponseEntity.ok(cityService.update(id, cityDTO));
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> disable(@PathVariable String id) {
        cityService.disable(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> enable(@PathVariable String id) {
        cityService.enable(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        cityService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/hotels")
    public ResponseEntity<List<HotelDTO>> getEnabledHotelsInCity(@PathVariable String id) {
        return ResponseEntity.ok(hotelService.getEnabledHotelsByCityId(id));
    }

    @GetMapping("/all/{id}/hotels")
    public ResponseEntity<List<HotelDTO>> getHotelsInCity(@PathVariable String id) {
        return ResponseEntity.ok(hotelService.getHotelsByCityId(id));
    }
}
