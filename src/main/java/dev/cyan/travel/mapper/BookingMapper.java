package dev.cyan.travel.mapper;

import dev.cyan.travel.DTO.BookingDTO;
import dev.cyan.travel.entity.Booking;
import dev.cyan.travel.entity.BookingState;
import dev.cyan.travel.repository.BookingStateRepository;
import dev.cyan.travel.repository.RoomRepository;
import dev.cyan.travel.repository.UserRepository;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {RoomRepository.class, UserRepository.class, BookingStateRepository.class})
public interface BookingMapper {
    @Mappings({
            @Mapping(source = "room.id", target = "roomId"),
            @Mapping(source = "user.id", target = "userId")
    })
    BookingDTO toDTO(Booking booking);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "state", ignore = true),
            @Mapping(source = "roomId", target = "room"),
            @Mapping(source = "userId", target = "user")
    })
    Booking fromDTO(BookingDTO bookingDTO);

    default String stateToString(BookingState bookingState) {
        return bookingState.getName().toString();
    }

    @InheritConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBooking(@MappingTarget Booking target, BookingDTO source);
}
