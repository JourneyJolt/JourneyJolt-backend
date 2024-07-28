package dev.cyan.travel.mapper;

import dev.cyan.travel.DTO.UserDTO;
import dev.cyan.travel.entity.Role;
import dev.cyan.travel.entity.User;
import dev.cyan.travel.repository.RoleRepository;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoleRepository.class})
public interface UserMapper {
    UserDTO toDTO(User user);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "password", ignore = true)
    })
    User fromDTO(UserDTO userDTO);

    Set<String> rolesToStrings(Set<Role> value);

    default String roleToString(Role role) {
        return role.getName().toString();
    }

    @InheritConfiguration
    Set<Role> stringsToRoles(Set<String> value);
}
