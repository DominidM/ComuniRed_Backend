package com.comunired.notificaciones.infrastructure.repository;

import com.comunired.notificaciones.domain.entity.Notificacion;
import com.comunired.notificaciones.domain.repository.NotificacionRepository;
import com.comunired.notificaciones.infrastructure.model.NotificacionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public class NotificacionRepositoryImpl implements NotificacionRepository {

    @Autowired
    private NotificacionMongoRepository mongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Notificacion save(Notificacion notificacion) {
        NotificacionModel model = toModel(notificacion);
        NotificacionModel saved = mongoRepository.save(model);
        return toEntity(saved);
    }

    @Override
    public Optional<Notificacion> findById(String id) {
        return mongoRepository.findById(id).map(this::toEntity);
    }

    @Override
    public Page<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(String usuarioId, Pageable pageable) {
        return mongoRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId, pageable)
                .map(this::toEntity);
    }

    @Override
    public long countByUsuarioIdAndLeida(String usuarioId, boolean leida) {
        return mongoRepository.countByUsuarioIdAndLeida(usuarioId, leida);
    }

    @Override
    public void marcarComoLeida(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("leida", true);
        update.set("fechaLectura", Instant.now());
        mongoTemplate.updateFirst(query, update, NotificacionModel.class);
    }

    @Override
    public void marcarTodasComoLeidas(String usuarioId) {
        Query query = new Query(Criteria.where("usuarioId").is(usuarioId).and("leida").is(false));
        Update update = new Update();
        update.set("leida", true);
        update.set("fechaLectura", Instant.now());
        mongoTemplate.updateMulti(query, update, NotificacionModel.class);
    }

    private NotificacionModel toModel(Notificacion entity) {
        NotificacionModel model = new NotificacionModel();
        model.setId(entity.getId());
        model.setUsuarioId(entity.getUsuarioId());
        model.setTipo(entity.getTipo());
        model.setTitulo(entity.getTitulo());
        model.setCuerpo(entity.getCuerpo());
        model.setReferenciaId(entity.getReferenciaId());
        model.setLeida(entity.getLeida());
        model.setFechaCreacion(entity.getFechaCreacion());
        model.setFechaLectura(entity.getFechaLectura());
        return model;
    }

    private Notificacion toEntity(NotificacionModel model) {
        Notificacion entity = new Notificacion();
        entity.setId(model.getId());
        entity.setUsuarioId(model.getUsuarioId());
        entity.setTipo(model.getTipo());
        entity.setTitulo(model.getTitulo());
        entity.setCuerpo(model.getCuerpo());
        entity.setReferenciaId(model.getReferenciaId());
        entity.setLeida(model.getLeida());
        entity.setFechaCreacion(model.getFechaCreacion());
        entity.setFechaLectura(model.getFechaLectura());
        return entity;
    }
}
