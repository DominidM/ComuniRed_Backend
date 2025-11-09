package com.comunired.mensajeria.infrastructure.repository;

import com.comunired.mensajeria.domain.entity.Mensaje;
import com.comunired.mensajeria.domain.repository.MensajeRepository;
import com.comunired.mensajeria.infrastructure.model.MensajeModel;
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
public class MensajeRepositoryImpl implements MensajeRepository {
    
    @Autowired
    private MensajeMongoRepository mongoRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Mensaje save(Mensaje mensaje) {
        MensajeModel model = toModel(mensaje);
        MensajeModel saved = mongoRepository.save(model);
        return toEntity(saved);
    }

    @Override
    public Optional<Mensaje> findById(String id) {
        return mongoRepository.findById(id)
                .map(this::toEntity);
    }

    @Override
    public Page<Mensaje> findByConversacionId(String conversacionId, Pageable pageable) {
        return mongoRepository.findByConversacionId(conversacionId, pageable)
                .map(this::toEntity);
    }

    @Override
    public long countNoLeidosByConversacionAndReceptor(String conversacionId, String receptorId) {
        return mongoRepository.countNoLeidosByConversacionAndReceptor(conversacionId, receptorId);
    }

    @Override
    public void marcarComoLeidos(String conversacionId, String receptorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("conversacionId").is(conversacionId)
                .and("emisorId").ne(receptorId)
                .and("leido").is(false));
        
        Update update = new Update();
        update.set("leido", true);
        update.set("fechaLectura", Instant.now());
        
        mongoTemplate.updateMulti(query, update, MensajeModel.class);
    }

    // Mapeo Model <-> Entity
    private MensajeModel toModel(Mensaje entity) {
        MensajeModel model = new MensajeModel();
        model.setId(entity.getId());
        model.setConversacionId(entity.getConversacionId());
        model.setEmisorId(entity.getEmisorId());
        model.setContenido(entity.getContenido());
        model.setFechaEnvio(entity.getFechaEnvio());
        model.setLeido(entity.getLeido());
        model.setFechaLectura(entity.getFechaLectura());
        return model;
    }

    private Mensaje toEntity(MensajeModel model) {
        Mensaje entity = new Mensaje();
        entity.setId(model.getId());
        entity.setConversacionId(model.getConversacionId());
        entity.setEmisorId(model.getEmisorId());
        entity.setContenido(model.getContenido());
        entity.setFechaEnvio(model.getFechaEnvio());
        entity.setLeido(model.getLeido());
        entity.setFechaLectura(model.getFechaLectura());
        return entity;
    }
}
