package com.comunired.reacciones.domain.repository;

import com.comunired.reacciones.domain.entity.Reacciones;
import java.util.List;
import java.util.Optional;

public interface ReaccionesRepository {
    Reacciones save(Reacciones reaccion);
    Optional<Reacciones> findById(String id);
    List<Reacciones> findByQuejaId(String quejaId);
    List<Reacciones> findByUsuarioId(String usuarioId);
    Optional<Reacciones> findByQuejaIdAndUsuarioId(String quejaId, String usuarioId);
    Optional<Reacciones> findByQuejaIdAndUsuarioIdAndTipoReaccionId(String quejaId, String usuarioId, String tipoReaccionId);
    long countByQuejaIdAndTipoReaccionId(String quejaId, String tipoReaccionId);
    void deleteById(String id);
    void delete(Reacciones reaccion);
    boolean existsById(String id);
}
