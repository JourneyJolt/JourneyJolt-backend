package dev.cyan.travel.entity;

import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "photos")
public class Photo {
    @MongoId
    private String id;
    private String name;
    private String type;
    private Binary image;
}
