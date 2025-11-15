package com.comunired.comentarios.infrastructure.repository;

import com.comunired.comentarios.infrastructure.model.ComentariosModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComentariosMongoRepository extends MongoRepository<ComentariosModel, String> {
    
    @Query("{ 'queja_id': ?0 }")
    List<ComentariosModel> findByQuejaIdOrderByFechaCreacionAsc(String quejaId);
    
    @Query("{ 'usuario_id': ?0 }")
    List<ComentariosModel> findByUsuarioId(String usuarioId);
    
    @Query(value = "{ 'queja_id': ?0 }", count = true)
    long countByQuejaId(String quejaId);
}
