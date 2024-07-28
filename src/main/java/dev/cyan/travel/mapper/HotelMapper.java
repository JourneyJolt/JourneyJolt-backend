package dev.cyan.travel.mapper;

import dev.cyan.travel.DTO.HotelDTO;
import dev.cyan.travel.entity.Hotel;
import dev.cyan.travel.entity.Photo;
import dev.cyan.travel.repository.CityRepository;
import dev.cyan.travel.repository.CountryRepository;
import dev.cyan.travel.repository.PhotoRepository;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {CountryRepository.class, CityRepository.class, PhotoRepository.class})
public interface HotelMapper {
    @Mappings({
            @Mapping(source = "country.id", target = "countryId"),
            @Mapping(source = "city.id", target = "cityId"),
            @Mapping(source = "photos", target = "photosIds")
    })
    HotelDTO toDTO(Hotel hotel);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(source = "countryId", target = "country"),
            @Mapping(source = "cityId", target = "city"),
            @Mapping(source = "photosIds", target = "photos")
    })
    Hotel fromDTO(HotelDTO hotelDTO);

    @InheritConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHotel(@MappingTarget Hotel target, HotelDTO source);

    Set<String> photosToIds(Set<Photo> value);

    default String photoToId(Photo photo) {
        return photo.getId();
    }

    @InheritConfiguration
    Set<Photo> idsToPhotos(Set<String> value);
}
