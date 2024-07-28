package dev.cyan.travel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "cities")
public class City {
    @MongoId
    private String id;
    @Indexed(unique = true)
    private String name;
    @DBRef
    private Country country;
    private boolean enabled;
}
