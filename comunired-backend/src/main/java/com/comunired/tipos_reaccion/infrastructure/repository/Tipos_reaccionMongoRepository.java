package com.comunired.tipos_reaccion.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion;
import java.util.Optional;

@Repository
public interface Tipos_reaccionMongoRepository extends MongoRepository<Tipos_reaccion, String> {

    // 🔹 Permite buscar un tipo de reacción por su "label"
    Optional<Tipos_reaccion> findByLabel(String label);
}
