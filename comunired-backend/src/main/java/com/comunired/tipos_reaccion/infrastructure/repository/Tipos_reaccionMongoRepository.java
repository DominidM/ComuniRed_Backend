package com.comunired.tipos_reaccion.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface Tipos_reaccionMongoRepository extends MongoRepository<Tipos_reaccion, String> {

    Optional<Tipos_reaccion> findByLabel(String label);
    Page<Tipos_reaccion> findAll(Pageable pageable);

}
