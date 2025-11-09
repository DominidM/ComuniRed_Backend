package com.comunired.seguimientos.infrastructure.repository;

import com.comunired.seguimientos.infrastructure.model.SeguimientoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeguimientoMongoRepository extends MongoRepository<SeguimientoModel, String> {
    // Métodos existentes
    Page<SeguimientoModel> findBySeguidorId(String seguidorId, Pageable pageable);
    Page<SeguimientoModel> findBySeguidoId(String seguidoId, Pageable pageable);
    boolean existsBySeguidorIdAndSeguidoId(String seguidorId, String seguidoId);
    void deleteBySeguidorIdAndSeguidoId(String seguidorId, String seguidoId);
    long countBySeguidoId(String seguidoId);
    long countBySeguidorId(String seguidorId);
    
    // Nuevos métodos con estado
    Page<SeguimientoModel> findBySeguidoIdAndEstado(String seguidoId, String estado, Pageable pageable);
    Page<SeguimientoModel> findBySeguidorIdAndEstado(String seguidorId, String estado, Pageable pageable);
    boolean existsBySeguidorIdAndSeguidoIdAndEstado(String seguidorId, String seguidoId, String estado);
    long countBySeguidoIdAndEstado(String seguidoId, String estado);
    long countBySeguidorIdAndEstado(String seguidorId, String estado);
}
