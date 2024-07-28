package dev.cyan.travel.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomDTO {
    private String id;
    @Min(value = 1, message = "room number must be greater than 0")
    private Integer roomNumber;
    @Min(value = 1, message = "capacity must be greater than 0")
    @Max(value = 10, message = "capacity must be less than or equal to 10")
    private Integer capacity;
    @Min(value = 100, message = "price must be greater than 0")
    @Max(value = 10_000, message = "price must be less than or equal to 10000")
    private Integer price;
    @NotBlank
    private String hotelId;
    private Boolean enabled;
}
