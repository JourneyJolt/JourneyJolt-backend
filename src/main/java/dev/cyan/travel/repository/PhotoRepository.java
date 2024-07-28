package dev.cyan.travel.repository;

import dev.cyan.travel.entity.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends MongoRepository<Photo, String> {
    Photo getById(String id);
}
