package com.comunired.mensajeria.infrastructure.repository;

import com.comunired.mensajeria.infrastructure.model.MensajeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeMongoRepository extends MongoRepository<MensajeModel, String> {
    
    @Query(value = "{ 'conversacionId': ?0 }", sort = "{ 'fechaEnvio': -1 }")
    Page<MensajeModel> findByConversacionId(String conversacionId, Pageable pageable);
    
    @Query(value = "{ 'conversacionId': ?0, 'emisorId': { $ne: ?1 }, 'leido': false }", count = true)
    long countNoLeidosByConversacionAndReceptor(String conversacionId, String receptorId);
}
