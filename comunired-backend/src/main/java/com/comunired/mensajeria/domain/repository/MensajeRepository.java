package com.comunired.mensajeria.domain.repository;

import com.comunired.mensajeria.domain.entity.Mensaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MensajeRepository {
    Mensaje save(Mensaje mensaje);
    Optional<Mensaje> findById(String id);
    Page<Mensaje> findByConversacionId(String conversacionId, Pageable pageable);
    long countNoLeidosByConversacionAndReceptor(String conversacionId, String receptorId);
    void marcarComoLeidos(String conversacionId, String receptorId);
}
