package com.comunired.historial_evento.infrastructure.repository;

import com.comunired.historial_evento.domain.entity.HistorialEvento;
import com.comunired.historial_evento.domain.repository.HistorialEventoRepository;
import com.comunired.historial_evento.infrastructure.model.HistorialEventoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class HistorialEventoRepositoryImpl implements HistorialEventoRepository {

    @Autowired
    private HistorialEventoMongoRepository mongoRepository;

    @Override
    public HistorialEvento save(HistorialEvento evento) {
        HistorialEventoModel model = toModel(evento);
        HistorialEventoModel saved = mongoRepository.save(model);
        return toEntity(saved);
    }

    @Override
    public Optional<HistorialEvento> findById(String id) {
        return mongoRepository.findById(id).map(this::toEntity);
    }

    @Override
    public List<HistorialEvento> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialEvento> findByQuejaId(String quejaId) {
        return mongoRepository.findByQuejaIdOrderByFechaAsc(quejaId)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialEvento> findByQuejaIdAndTipoEvento(String quejaId, String tipoEvento) {
        return mongoRepository.findByQuejaIdAndTipoEvento(quejaId, tipoEvento)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialEvento> findByUsuarioId(String usuarioId) {
        return mongoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId)
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

    private HistorialEventoModel toModel(HistorialEvento entity) {
        HistorialEventoModel model = new HistorialEventoModel();
        model.setId(entity.getId());
        model.setQueja_id(entity.getQueja_id());
        model.setUsuario_id(entity.getUsuario_id());
        model.setTipo_evento(entity.getTipo_evento());
        model.setEstado_anterior(entity.getEstado_anterior());
        model.setEstado_nuevo(entity.getEstado_nuevo());
        model.setDescripcion(entity.getDescripcion());
        model.setFecha_evento(entity.getFecha_evento());
        return model;
    }

    private HistorialEvento toEntity(HistorialEventoModel model) {
        HistorialEvento entity = new HistorialEvento();
        entity.setId(model.getId());
        entity.setQueja_id(model.getQueja_id());
        entity.setUsuario_id(model.getUsuario_id());
        entity.setTipo_evento(model.getTipo_evento());
        entity.setEstado_anterior(model.getEstado_anterior());
        entity.setEstado_nuevo(model.getEstado_nuevo());
        entity.setDescripcion(model.getDescripcion());
        entity.setFecha_evento(model.getFecha_evento());
        return entity;
    }
}
