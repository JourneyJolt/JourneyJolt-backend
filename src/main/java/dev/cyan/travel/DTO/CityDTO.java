package dev.cyan.travel.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CityDTO {
    private String id;
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
    @NotBlank
    private String countryId;
    private Boolean enabled;
}
