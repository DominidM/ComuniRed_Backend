package com.comunired.asignaciones.infrastructure.repository;

import com.comunired.asignaciones.infrastructure.model.AsignacionesModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionesMongoRepository extends MongoRepository<AsignacionesModel, String> {
    
    @Query("{ 'queja_id': ?0 }")
    Optional<AsignacionesModel> findByQuejaId(String quejaId);
    
    @Query("{ 'asignado_por_id': ?0 }")
    List<AsignacionesModel> findByAdminId(String adminId);
    
    @Query("{ 'soporte_id': ?0 }")
    List<AsignacionesModel> findBySoporteId(String soporteId);
    
    @Query("{ 'estado': ?0 }")
    List<AsignacionesModel> findByEstado(String estado);
}
