package dev.cyan.travel.entity;

import dev.cyan.travel.enums.ERole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "roles")
public class Role {
    @MongoId
    private String id;
    private ERole name;
}
