package com.comunired.asignaciones.domain.repository;

import com.comunired.asignaciones.domain.entity.Asignaciones;
import java.util.List;
import java.util.Optional;

public interface AsignacionesRepository {
    Asignaciones save(Asignaciones asignacion);
    Optional<Asignaciones> findById(String id);
    Optional<Asignaciones> findByQuejaId(String quejaId);
    List<Asignaciones> findByAdminId(String adminId);
    List<Asignaciones> findBySoporteId(String soporteId);
    List<Asignaciones> findByEstado(String estado);
    void deleteById(String id);
}
