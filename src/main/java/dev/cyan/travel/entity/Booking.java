package dev.cyan.travel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "bookings")
public class Booking {
    @MongoId
    private String id;
    private LocalDate bookedSince;
    private LocalDate bookedTo;
    private int price;
    @DBRef
    private Room room;
    @DBRef
    private User user;
    @DocumentReference
    private BookingState state;
}
