package com.comunired.asignaciones.infrastructure.repository;

import com.comunired.asignaciones.domain.entity.Asignaciones;
import com.comunired.asignaciones.domain.repository.AsignacionesRepository;
import com.comunired.asignaciones.infrastructure.model.AsignacionesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AsignacionesRepositoryImpl implements AsignacionesRepository {

    @Autowired
    private AsignacionesMongoRepository mongoRepository;

    @Override
    public Asignaciones save(Asignaciones asignacion) {
        AsignacionesModel model = toModel(asignacion);
        AsignacionesModel saved = mongoRepository.save(model);
        return toEntity(saved);
    }

    @Override
    public Optional<Asignaciones> findById(String id) {
        return mongoRepository.findById(id).map(this::toEntity);
    }

    @Override
    public Optional<Asignaciones> findByQuejaId(String quejaId) {
        return mongoRepository.findByQuejaId(quejaId).map(this::toEntity);
    }

    @Override
    public List<Asignaciones> findByAdminId(String adminId) {
        return mongoRepository.findByAdminId(adminId)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Asignaciones> findBySoporteId(String soporteId) {
        return mongoRepository.findBySoporteId(soporteId)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Asignaciones> findByEstado(String estado) {
        return mongoRepository.findByEstado(estado)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    private AsignacionesModel toModel(Asignaciones entity) {
        AsignacionesModel model = new AsignacionesModel();
        model.setId(entity.getId());
        model.setQueja_id(entity.getQueja_id());
        model.setSoporte_id(entity.getSoporte_id());
        model.setAsignado_por_id(entity.getAsignado_por_id());
        model.setEstado(entity.getEstado());
        model.setFecha_asignacion(entity.getFecha_asignacion());
        model.setFecha_actualizacion(entity.getFecha_actualizacion());
        model.setObservacion(entity.getObservacion());
        return model;
    }

    private Asignaciones toEntity(AsignacionesModel model) {
        Asignaciones entity = new Asignaciones();
        entity.setId(model.getId());
        entity.setQueja_id(model.getQueja_id());
        entity.setSoporte_id(model.getSoporte_id());
        entity.setAsignado_por_id(model.getAsignado_por_id());
        entity.setEstado(model.getEstado());
        entity.setFecha_asignacion(model.getFecha_asignacion());
        entity.setFecha_actualizacion(model.getFecha_actualizacion());
        entity.setObservacion(model.getObservacion());
        return entity;
    }
}
