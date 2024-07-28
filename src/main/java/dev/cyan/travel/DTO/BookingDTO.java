package dev.cyan.travel.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BookingDTO {
    private String id;
    @FutureOrPresent(message = "not valid bookedSince date")
    private LocalDate bookedSince;
    @FutureOrPresent(message = "not valid bookedTo date")
    private LocalDate bookedTo;
    @Min(value = 100, message = "price must be greater than 0")
    private int price;
    @NotBlank
    private String roomId;
    @NotBlank
    private String userId;
    private String state;

    public BookingDTO(String state) {
        this.state = state;
    }
}
