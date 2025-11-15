package com.comunired.quejas.infrastructure.repository;

import com.comunired.quejas.infrastructure.model.QuejasModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuejasMongoRepository extends MongoRepository<QuejasModel, String> {
    
    @Query("{ 'usuario_id': ?0 }")
    List<QuejasModel> findByUsuarioId(String usuarioId);
    
    @Query("{ 'categoria_id': ?0 }")
    List<QuejasModel> findByCategoriaId(String categoriaId);
    
    @Query("{ 'estado_id': ?0 }")
    List<QuejasModel> findByEstadoId(String estadoId);
    
    @Query(value = "{}", sort = "{ 'fecha_creacion': -1 }")
    List<QuejasModel> findAllByOrderByFechaCreacionDesc();
}
