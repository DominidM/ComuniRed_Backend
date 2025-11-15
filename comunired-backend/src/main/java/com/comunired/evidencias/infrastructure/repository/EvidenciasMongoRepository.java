package com.comunired.evidencias.infrastructure.repository;

import com.comunired.evidencias.infrastructure.model.EvidenciasModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvidenciasMongoRepository extends MongoRepository<EvidenciasModel, String> {
    
    @Query("{ 'queja_id': ?0 }")
    List<EvidenciasModel> findByQuejaId(String quejaId);
    
    @Query(value = "{ 'queja_id': ?0 }", delete = true)
    void deleteByQuejaId(String quejaId);
}
