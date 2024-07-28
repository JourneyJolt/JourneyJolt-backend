package dev.cyan.travel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "countries")
public class Country {
    @MongoId
    private String id;
    @Indexed(unique = true)
    private String name;
    private boolean enabled;
}
