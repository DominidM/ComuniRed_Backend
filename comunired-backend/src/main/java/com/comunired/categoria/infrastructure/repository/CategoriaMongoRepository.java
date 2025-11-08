package com.comunired.categoria.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.comunired.categoria.infrastructure.entity.Categoria;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CategoriaMongoRepository extends MongoRepository<Categoria, String> {
    Optional<Categoria> findByNombre(String nombre);
    List<Categoria> findAllByActivoTrue();
    Page<Categoria> findAll(Pageable pageable);
}