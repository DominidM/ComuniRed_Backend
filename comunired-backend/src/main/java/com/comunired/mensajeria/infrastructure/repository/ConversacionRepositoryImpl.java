package com.comunired.mensajeria.infrastructure.repository;

import com.comunired.mensajeria.domain.entity.Conversacion;
import com.comunired.mensajeria.domain.repository.ConversacionRepository;
import com.comunired.mensajeria.infrastructure.model.ConversacionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ConversacionRepositoryImpl implements ConversacionRepository {
    
    @Autowired
    private ConversacionMongoRepository mongoRepository;

    @Override
    public Conversacion save(Conversacion conversacion) {
        ConversacionModel model = toModel(conversacion);
        ConversacionModel saved = mongoRepository.save(model);
        return toEntity(saved);
    }

    @Override
    public Optional<Conversacion> findById(String id) {
        return mongoRepository.findById(id)
                .map(this::toEntity);
    }

    @Override
    public Optional<Conversacion> findByParticipantes(String userId1, String userId2) {
        return mongoRepository.findByParticipantes(userId1, userId2)
                .map(this::toEntity);
    }

    @Override
    public Page<Conversacion> findByUsuarioId(String usuarioId, Pageable pageable) {
        return mongoRepository.findByUsuarioId(usuarioId, pageable)
                .map(this::toEntity);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return mongoRepository.existsById(id);
    }

    // Mapeo Model <-> Entity
    private ConversacionModel toModel(Conversacion entity) {
        ConversacionModel model = new ConversacionModel();
        model.setId(entity.getId());
        model.setParticipante1Id(entity.getParticipante1Id());
        model.setParticipante2Id(entity.getParticipante2Id());
        model.setUltimoMensajeId(entity.getUltimoMensajeId());
        model.setFechaCreacion(entity.getFechaCreacion());
        model.setFechaUltimaActividad(entity.getFechaUltimaActividad());
        return model;
    }

    private Conversacion toEntity(ConversacionModel model) {
        Conversacion entity = new Conversacion();
        entity.setId(model.getId());
        entity.setParticipante1Id(model.getParticipante1Id());
        entity.setParticipante2Id(model.getParticipante2Id());
        entity.setUltimoMensajeId(model.getUltimoMensajeId());
        entity.setFechaCreacion(model.getFechaCreacion());
        entity.setFechaUltimaActividad(model.getFechaUltimaActividad());
        return entity;
    }
}
