package com.comunired.historial_evento.infrastructure.repository;

import com.comunired.historial_evento.infrastructure.model.HistorialEventoModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialEventoMongoRepository extends MongoRepository<HistorialEventoModel, String> {
    
    @Query("{ 'queja_id': ?0 }")
    List<HistorialEventoModel> findByQuejaIdOrderByFechaAsc(String quejaId);
    
    @Query("{ 'queja_id': ?0, 'tipo_evento': ?1 }")
    List<HistorialEventoModel> findByQuejaIdAndTipoEvento(String quejaId, String tipoEvento);
    
    @Query("{ 'usuario_id': ?0 }")
    List<HistorialEventoModel> findByUsuarioIdOrderByFechaDesc(String usuarioId);
}
