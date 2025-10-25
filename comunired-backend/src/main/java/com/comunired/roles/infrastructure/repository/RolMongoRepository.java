package com.comunired.roles.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.comunired.roles.infrastructure.entity.RolEntity;

@Repository
public interface RolMongoRepository extends MongoRepository<RolEntity, String> {
    Optional<RolEntity> findByNombre(String nombre);
}