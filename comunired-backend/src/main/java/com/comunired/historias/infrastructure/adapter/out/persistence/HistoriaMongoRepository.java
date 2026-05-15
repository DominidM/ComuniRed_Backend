package com.comunired.historias.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;

public interface HistoriaMongoRepository extends MongoRepository<HistoriaDocument, String> {
    List<HistoriaDocument> findByActivaTrue();

    @Query("{ 'usuarioId': ?0, 'texto': ?1, 'colorFondo': ?2, 'activa': true, 'fechaCreacion': { $gte: ?3 } }")
    List<HistoriaDocument> buscarDuplicados(String usuarioId, String texto, String colorFondo, Instant desde);
}