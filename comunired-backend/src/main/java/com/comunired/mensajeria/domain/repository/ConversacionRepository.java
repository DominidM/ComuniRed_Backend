package com.comunired.mensajeria.domain.repository;

import com.comunired.mensajeria.domain.entity.Conversacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ConversacionRepository {
    Conversacion save(Conversacion conversacion);
    Optional<Conversacion> findById(String id);
    Optional<Conversacion> findByParticipantes(String userId1, String userId2);
    Page<Conversacion> findByUsuarioId(String usuarioId, Pageable pageable);
    void deleteById(String id);
    boolean existsById(String id);
}
