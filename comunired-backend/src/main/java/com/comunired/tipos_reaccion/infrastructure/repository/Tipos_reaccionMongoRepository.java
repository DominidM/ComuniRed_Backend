package com.comunired.tipos_reaccion.infrastructure.repository;

import com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface Tipos_reaccionMongoRepository extends MongoRepository<Tipos_reaccion, String> {

    @Query("{ 'label': ?0 }")
    Optional<Tipos_reaccion> findByLabel(String label);

    @Query("{ 'key': ?0 }")
    Optional<Tipos_reaccion> findByKey(String key);
}