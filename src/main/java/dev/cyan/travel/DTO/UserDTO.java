package dev.cyan.travel.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String email;
    @NotEmpty
    private Set<String> roles;
    private Boolean enabled;
}
