package dev.cyan.travel.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class HotelDTO {
    private String id;
    @NotBlank
    @Size(min = 3, max = 35)
    private String name;
    @NotBlank
    private String countryId;
    @NotBlank
    private String cityId;
    private Set<String> photosIds;
    private Boolean enabled;
}
