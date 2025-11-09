package com.comunired.mensajeria.infrastructure.repository;

import com.comunired.mensajeria.infrastructure.model.ConversacionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversacionMongoRepository extends MongoRepository<ConversacionModel, String> {
    
    @Query("{ $or: [ " +
           "{ $and: [ { 'participante1Id': ?0 }, { 'participante2Id': ?1 } ] }, " +
           "{ $and: [ { 'participante1Id': ?1 }, { 'participante2Id': ?0 } ] } " +
           "] }")
    Optional<ConversacionModel> findByParticipantes(String userId1, String userId2);
    
    @Query("{ $or: [ { 'participante1Id': ?0 }, { 'participante2Id': ?0 } ] }")
    Page<ConversacionModel> findByUsuarioId(String usuarioId, Pageable pageable);
}
