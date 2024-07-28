package dev.cyan.travel.mapper;

import dev.cyan.travel.DTO.CityDTO;
import dev.cyan.travel.entity.City;
import dev.cyan.travel.repository.CountryRepository;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CountryRepository.class})
public interface CityMapper {
    @Mapping(source = "country.id", target = "countryId")
    CityDTO toDTO(City city);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(source = "countryId", target = "country")
    })
    City fromDTO(CityDTO cityDTO);

    @InheritConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCity(@MappingTarget City target, CityDTO source);
}
