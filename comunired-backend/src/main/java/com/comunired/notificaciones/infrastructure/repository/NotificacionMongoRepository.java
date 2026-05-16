package com.comunired.notificaciones.infrastructure.repository;

import com.comunired.notificaciones.infrastructure.model.NotificacionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionMongoRepository extends MongoRepository<NotificacionModel, String> {

    Page<NotificacionModel> findByUsuarioIdOrderByFechaCreacionDesc(String usuarioId, Pageable pageable);

    long countByUsuarioIdAndLeida(String usuarioId, boolean leida);
}
