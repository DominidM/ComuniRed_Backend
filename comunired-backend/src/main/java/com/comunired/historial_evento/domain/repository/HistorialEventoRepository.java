package com.comunired.historial_evento.domain.repository;

import com.comunired.historial_evento.domain.entity.HistorialEvento;
import java.util.List;
import java.util.Optional;

public interface HistorialEventoRepository {
    HistorialEvento save(HistorialEvento evento);
    Optional<HistorialEvento> findById(String id);
    List<HistorialEvento> findAll();
    List<HistorialEvento> findByQuejaId(String quejaId);
    List<HistorialEvento> findByQuejaIdAndTipoEvento(String quejaId, String tipoEvento);
    List<HistorialEvento> findByUsuarioId(String usuarioId);
    void deleteById(String id);
    boolean existsById(String id);
}
