package com.comunired.notificaciones.domain.repository;

import com.comunired.notificaciones.domain.entity.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificacionRepository {
    Notificacion save(Notificacion notificacion);
    Optional<Notificacion> findById(String id);
    Page<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(String usuarioId, Pageable pageable);
    long countByUsuarioIdAndLeida(String usuarioId, boolean leida);
    void marcarComoLeida(String id);
    void marcarTodasComoLeidas(String usuarioId);
}
