package dev.cyan.travel.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountryDTO {
    private String id;
    @NotBlank
    @Size(min = 2, max = 20)
    private String name;
    private Boolean enabled;
}
