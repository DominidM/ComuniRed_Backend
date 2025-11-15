package com.comunired.quejas.infrastructure.repository;

import com.comunired.quejas.domain.entity.Quejas;
import com.comunired.quejas.domain.repository.QuejasRepository;
import com.comunired.quejas.infrastructure.model.QuejasModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class QuejasRepositoryImpl implements QuejasRepository {

    @Autowired
    private QuejasMongoRepository mongoRepository;

    @Override
    public Quejas save(Quejas quejas) {
        QuejasModel model = toModel(quejas);
        model.setFecha_actualizacion(Instant.now());
        QuejasModel saved = mongoRepository.save(model);
        return toEntity(saved);
    }

    @Override
    public Optional<Quejas> findById(String id) {
        return mongoRepository.findById(id).map(this::toEntity);
    }

    @Override
    public List<Quejas> findAll() {
        return mongoRepository.findAllByOrderByFechaCreacionDesc()
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quejas> findByUsuarioId(String usuarioId) {
        return mongoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quejas> findByCategoriaId(String categoriaId) {
        return mongoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quejas> findByEstadoId(String estadoId) {
        return mongoRepository.findByEstadoId(estadoId)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return mongoRepository.existsById(id);
    }

    @Override
    public long count() {
        return mongoRepository.count();
    }

    private QuejasModel toModel(Quejas entity) {
        QuejasModel model = new QuejasModel();
        model.setId(entity.getId());
        model.setTitulo(entity.getTitulo());
        model.setDescripcion(entity.getDescripcion());
        model.setUsuario_id(entity.getUsuario_id());
        model.setCategoria_id(entity.getCategoria_id());
        model.setEstado_id(entity.getEstado_id());
        model.setUbicacion(entity.getUbicacion());
        model.setImagen_url(entity.getImagen_url());
        model.setFecha_creacion(entity.getFecha_creacion());
        model.setFecha_actualizacion(entity.getFecha_actualizacion());
        return model;
    }

    private Quejas toEntity(QuejasModel model) {
        Quejas entity = new Quejas();
        entity.setId(model.getId());
        entity.setTitulo(model.getTitulo());
        entity.setDescripcion(model.getDescripcion());
        entity.setUsuario_id(model.getUsuario_id());
        entity.setCategoria_id(model.getCategoria_id());
        entity.setEstado_id(model.getEstado_id());
        entity.setUbicacion(model.getUbicacion());
        entity.setImagen_url(model.getImagen_url());
        entity.setFecha_creacion(model.getFecha_creacion());
        entity.setFecha_actualizacion(model.getFecha_actualizacion());
        return entity;
    }
}
