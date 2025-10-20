package com.comunired.categoria.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.comunired.categoria.domain.entity.Categoria;

@Repository
public interface CategoriaMongoRepository extends MongoRepository<Categoria, String> {
    Optional<Categoria> findByNombre(String nombre);
    List<Categoria> findAllByActivoTrue();
}