package dev.cyan.travel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "rooms")
public class Room {
    @MongoId
    private String id;
    private int roomNumber;
    private int capacity;
    private int price;
    @DBRef
    private Hotel hotel;
    private boolean enabled;
}
