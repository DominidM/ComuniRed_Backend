package com.comunired.evidencias.infrastructure.repository;

import com.comunired.evidencias.domain.entity.Evidencias;
import com.comunired.evidencias.domain.repository.EvidenciasRepository;
import com.comunired.evidencias.infrastructure.model.EvidenciasModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EvidenciasRepositoryImpl implements EvidenciasRepository {

    @Autowired
    private EvidenciasMongoRepository mongoRepository;

    @Override
    public Evidencias save(Evidencias evidencia) {
        EvidenciasModel model = toModel(evidencia);
        EvidenciasModel saved = mongoRepository.save(model);
        return toEntity(saved);
    }

    @Override
    public Optional<Evidencias> findById(String id) {
        return mongoRepository.findById(id).map(this::toEntity);
    }

    @Override
    public List<Evidencias> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Evidencias> findByQuejaId(String quejaId) {
        return mongoRepository.findByQuejaId(quejaId)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteByQuejaId(String quejaId) {
        mongoRepository.deleteByQuejaId(quejaId);
    }

    @Override
    public boolean existsById(String id) {
        return mongoRepository.existsById(id);
    }

    private EvidenciasModel toModel(Evidencias entity) {
        EvidenciasModel model = new EvidenciasModel();
        model.setId(entity.getId());
        model.setQueja_id(entity.getQueja_id());
        model.setUrl(entity.getUrl());
        model.setTipo(entity.getTipo());
        model.setFecha_subida(entity.getFecha_subida());
        return model;
    }

    private Evidencias toEntity(EvidenciasModel model) {
        Evidencias entity = new Evidencias();
        entity.setId(model.getId());
        entity.setQueja_id(model.getQueja_id());
        entity.setUrl(model.getUrl());
        entity.setTipo(model.getTipo());
        entity.setFecha_subida(model.getFecha_subida());
        return entity;
    }
}
