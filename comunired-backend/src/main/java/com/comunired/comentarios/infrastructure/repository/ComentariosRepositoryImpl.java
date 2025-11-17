package com.comunired.comentarios.infrastructure.repository;

import com.comunired.comentarios.domain.entity.Comentarios;
import com.comunired.comentarios.domain.repository.ComentariosRepository;
import com.comunired.comentarios.infrastructure.model.ComentariosModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ComentariosRepositoryImpl implements ComentariosRepository {

    @Autowired
    private ComentariosMongoRepository mongoRepository;

    @Override
    public Comentarios save(Comentarios comentario) {
        ComentariosModel model = toModel(comentario);
        ComentariosModel saved = mongoRepository.save(model);
        return toEntity(saved);
    }

    @Override
    public Optional<Comentarios> findById(String id) {
        return mongoRepository.findById(id).map(this::toEntity);
    }

    @Override
    public List<Comentarios> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comentarios> findByQuejaId(String quejaId) {
        return mongoRepository.findByQuejaIdOrderByFechaCreacionAsc(quejaId)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comentarios> findByUsuarioId(String usuarioId) {
        return mongoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long countByQuejaId(String quejaId) {
        return mongoRepository.countByQuejaId(quejaId);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return mongoRepository.existsById(id);
    }

    private ComentariosModel toModel(Comentarios entity) {
        ComentariosModel model = new ComentariosModel();
        model.setId(entity.getId());
        model.setQueja_id(entity.getQueja_id());
        model.setUsuario_id(entity.getUsuario_id());
        model.setTexto(entity.getTexto());
        model.setFecha_creacion(entity.getFecha_creacion());
        model.setFecha_modificacion(entity.getFecha_modificacion());
        return model;
    }

    

    private Comentarios toEntity(ComentariosModel model) {
        Comentarios entity = new Comentarios();
        entity.setId(model.getId());
        entity.setQueja_id(model.getQueja_id());
        entity.setUsuario_id(model.getUsuario_id());
        entity.setTexto(model.getTexto());
        entity.setFecha_creacion(model.getFecha_creacion());
        entity.setFecha_modificacion(model.getFecha_modificacion());
        return entity;
    }
}
