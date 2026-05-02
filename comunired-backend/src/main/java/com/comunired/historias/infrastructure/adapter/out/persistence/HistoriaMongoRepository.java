package com.comunired.historias.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoriaMongoRepository extends MongoRepository<HistoriaDocument, String> {
    List<HistoriaDocument> findByActivaTrue();
}