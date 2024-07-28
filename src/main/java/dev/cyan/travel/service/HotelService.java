package dev.cyan.travel.service;

import dev.cyan.travel.DTO.HotelDTO;
import dev.cyan.travel.DTO.RoomDTO;
import dev.cyan.travel.entity.*;
import dev.cyan.travel.mapper.HotelMapper;
import dev.cyan.travel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final CountryRepository countryRepository;
    private final RoomRepository roomRepository;
    private final HotelMapper hotelMapper;
    private final RoomService roomService;
    private final CityRepository cityRepository;
    private final PhotoRepository photoRepository;

    public List<HotelDTO> getAllEnabled() {
        return hotelRepository
                .findHotelsByEnabledIsTrue()
                .stream()
                .map(hotelMapper::toDTO)
                .toList();
    }

    public List<HotelDTO> getAll() {
        return hotelRepository
                .findAll()
                .stream()
                .map(hotelMapper::toDTO)
                .toList();
    }

    public Optional<HotelDTO> getById(String id) {
        return hotelRepository
                .findById(id)
                .map(hotelMapper::toDTO);
    }

    public Optional<HotelDTO> create(HotelDTO hotelDTO) {
        countryRepository.findById(hotelDTO.getCountryId()).orElseThrow();
        City city = cityRepository.findById(hotelDTO.getCityId()).orElseThrow();

        if (city.getCountry().getId().equals(hotelDTO.getCountryId())) {
            Hotel hotel = hotelMapper.fromDTO(hotelDTO);
            hotel.setEnabled(true);
            Hotel createdHotel = hotelRepository.save(hotel);
            return Optional.of(hotelMapper.toDTO(createdHotel));
        }

        return Optional.empty();
    }

    public Optional<HotelDTO> update(String id, HotelDTO hotelDTO) {
        countryRepository.findById(hotelDTO.getCountryId()).orElseThrow();
        City city = cityRepository.findById(hotelDTO.getCityId()).orElseThrow();

        if (city.getCountry().getId().equals(hotelDTO.getCountryId())) {
            Hotel hotel = hotelRepository
                    .findById(id)
                    .orElseThrow();
            hotelMapper.updateHotel(hotel, hotelDTO);
            Hotel modifiedHotel = hotelRepository.save(hotel);
            return Optional.of(hotelMapper.toDTO(modifiedHotel));
        }

        return Optional.empty();
    }

    public void disable(String id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow();
        hotel.setEnabled(false);

        List<Room> rooms = roomRepository.findRoomsByHotel(hotel);
        for (Room room : rooms) {
            roomService.disable(room.getId());
        }

        hotelRepository.save(hotel);
    }

    public void enable(String id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow();
        hotel.setEnabled(true);
        hotelRepository.save(hotel);
    }

    public void delete(String id) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow();

        List<Room> rooms = roomRepository.findRoomsByHotel(hotel);
        for (Room room : rooms) {
            roomService.delete(room.getId());
        }

        for (Photo photo : hotel.getPhotos()) {
            photoRepository.deleteById(photo.getId());
        }

        hotelRepository.deleteById(id);
    }

    public List<HotelDTO> getEnabledHotelsByCountryId(String id) {
        Country country = countryRepository.findById(id).orElseThrow();
        List<Hotel> hotelsByCountry = hotelRepository
                .findHotelsByEnabledIsTrueAndCountry(country);
        return hotelsByCountry
                .stream()
                .map(hotelMapper::toDTO)
                .toList();
    }

    public List<HotelDTO> getHotelsByCountryId(String id) {
        Country country = countryRepository.findById(id).orElseThrow();
        List<Hotel> hotelsByCountry = hotelRepository
                .findHotelsByCountry(country);
        return hotelsByCountry
                .stream()
                .map(hotelMapper::toDTO)
                .toList();
    }

    public List<HotelDTO> getEnabledHotelsByCityId(String id) {
        City city = cityRepository.findById(id).orElseThrow();
        List<Hotel> hotelsByCity = hotelRepository
                .findHotelsByEnabledIsTrueAndCity(city);
        return hotelsByCity
                .stream()
                .map(hotelMapper::toDTO)
                .toList();
    }

    public List<HotelDTO> getHotelsByCityId(String id) {
        City city = cityRepository.findById(id).orElseThrow();
        List<Hotel> hotelsByCity = hotelRepository
                .findHotelsByCity(city);
        return hotelsByCity
                .stream()
                .map(hotelMapper::toDTO)
                .toList();
    }

    public List<HotelDTO> getHotelsWithAvailableRooms(String countryId, String cityId, LocalDate bookedSince,
                                                      LocalDate bookedTo, Integer capacity) {
        List<Hotel> hotels;
        if (cityId != null) {
            City city = cityRepository.findById(cityId).orElseThrow();
            hotels = hotelRepository.findHotelsByEnabledIsTrueAndCity(city);
        } else if (countryId != null) {
            Country country = countryRepository.findById(countryId).orElseThrow();
            hotels = hotelRepository.findHotelsByEnabledIsTrueAndCountry(country);
        } else {
            hotels = hotelRepository.findHotelsByEnabledIsTrue();
        }

        ArrayList<Hotel> availableHotels = new ArrayList<>();
        for (Hotel hotel : hotels) {
            List<RoomDTO> availableRoomsInHotel =
                    roomService.getAllAvailableRoomsForPeriod(hotel.getId(), bookedSince, bookedTo, capacity);
            if (!availableRoomsInHotel.isEmpty()) {
                availableHotels.add(hotel);
            }
        }


        return availableHotels
                .stream()
                .map(hotelMapper::toDTO)
                .toList();
    }
}
