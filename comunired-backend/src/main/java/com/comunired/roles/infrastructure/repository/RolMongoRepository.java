package com.comunired.roles.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.comunired.roles.domain.entity.Rol;

@Repository
public interface RolMongoRepository extends MongoRepository<Rol, String> {
    Rol findByNombre(String nombre);
}