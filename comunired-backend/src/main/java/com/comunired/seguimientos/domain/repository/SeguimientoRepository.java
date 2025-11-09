package com.comunired.seguimientos.domain.repository;

import com.comunired.seguimientos.domain.entity.Seguimiento;
import com.comunired.seguimientos.domain.entity.Seguimiento.EstadoSeguimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SeguimientoRepository {
    Seguimiento save(Seguimiento seguimiento);
    Optional<Seguimiento> findById(String id);
    Page<Seguimiento> findBySeguidoId(String seguidoId, Pageable pageable);
    Page<Seguimiento> findBySeguidorId(String seguidorId, Pageable pageable);
    
    Page<Seguimiento> findBySeguidoIdAndEstado(String seguidoId, EstadoSeguimiento estado, Pageable pageable);
    Page<Seguimiento> findBySeguidorIdAndEstado(String seguidorId, EstadoSeguimiento estado, Pageable pageable);
    
    boolean existsBySeguidorIdAndSeguidoId(String seguidorId, String seguidoId);
    boolean existsBySeguidorIdAndSeguidoIdAndEstado(String seguidorId, String seguidoId, EstadoSeguimiento estado);
    
    void deleteBySeguidorIdAndSeguidoId(String seguidorId, String seguidoId);
    void deleteById(String id);
    
    long countBySeguidoId(String seguidoId);
    long countBySeguidorId(String seguidorId);
    long countBySeguidoIdAndEstado(String seguidoId, EstadoSeguimiento estado);
    long countBySeguidorIdAndEstado(String seguidorId, EstadoSeguimiento estado);
}
