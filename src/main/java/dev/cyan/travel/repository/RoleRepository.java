package dev.cyan.travel.repository;

import dev.cyan.travel.entity.Role;
import dev.cyan.travel.enums.ERole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);

    Role getByName(ERole name);
}
