package dev.cyan.travel.entity;

import dev.cyan.travel.enums.EBookingState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "bookingStates")
public class BookingState {
    @MongoId
    private String id;
    private EBookingState name;
}
