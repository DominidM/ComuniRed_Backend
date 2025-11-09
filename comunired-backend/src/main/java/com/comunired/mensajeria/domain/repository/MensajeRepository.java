package com.comunired.mensajeria.domain.repository;

import com.comunired.mensajeria.domain.entity.Mensaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MensajeRepository {
    Mensaje save(Mensaje mensaje);
    Page<Mensaje> findByConversacionId(String conversacionId, Pageable pageable);
    List<Mensaje> findByConversacionIdAndLeido(String conversacionId, Boolean leido);
    long countNoLeidosByReceptorId(String receptorId);
    void marcarComoLeidos(String conversacionId, String receptorId);
}
