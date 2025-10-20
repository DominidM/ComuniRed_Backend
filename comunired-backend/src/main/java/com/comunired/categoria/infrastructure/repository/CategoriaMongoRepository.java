package com.comunired.categoria.infrastructure.repository;

import com.comunired.categoria.domain.entity.categoria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaMongoRepository extends MongoRepository<categoria, String> {

    Optional<categoria> findByNombre(String nombre);
}
