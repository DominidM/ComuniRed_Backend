package com.comunired.reacciones.infrastructure.repository;

import com.comunired.reacciones.infrastructure.model.ReaccionesModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReaccionesMongoRepository extends MongoRepository<ReaccionesModel, String> {
    
    @Query("{ 'queja_id': ?0 }")
    List<ReaccionesModel> findByQuejaId(String quejaId);
    
    @Query("{ 'usuario_id': ?0 }")
    List<ReaccionesModel> findByUsuarioId(String usuarioId);
    
    @Query("{ 'queja_id': ?0, 'usuario_id': ?1 }")
    Optional<ReaccionesModel> findByQuejaIdAndUsuarioId(String quejaId, String usuarioId);
    
    @Query("{ 'queja_id': ?0, 'usuario_id': ?1, 'tipo_reaccion_id': ?2 }")
    Optional<ReaccionesModel> findByQuejaIdAndUsuarioIdAndTipoReaccionId(String quejaId, String usuarioId, String tipoReaccionId);
    
    @Query(value = "{ 'queja_id': ?0, 'tipo_reaccion_id': ?1 }", count = true)
    long countByQuejaIdAndTipoReaccionId(String quejaId, String tipoReaccionId);
    
    @Query(value = "{ 'usuario_id': ?0 }", count = true)
    long countByUsuarioId(String usuarioId);
}
