package dev.cyan.travel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "hotels")
public class Hotel {
    @MongoId
    private String id;
    private String name;
    @DBRef
    private Country country;
    @DBRef
    private City city;
    @DBRef
    private Set<Photo> photos = new HashSet<>();
    private boolean enabled;
}
