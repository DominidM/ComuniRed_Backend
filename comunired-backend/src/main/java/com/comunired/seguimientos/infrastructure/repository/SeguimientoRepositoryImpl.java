package com.comunired.seguimientos.infrastructure.repository;

import com.comunired.seguimientos.domain.entity.Seguimiento;
import com.comunired.seguimientos.domain.entity.Seguimiento.EstadoSeguimiento;
import com.comunired.seguimientos.domain.repository.SeguimientoRepository;
import com.comunired.seguimientos.infrastructure.model.SeguimientoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SeguimientoRepositoryImpl implements SeguimientoRepository {
    
    @Autowired
    private SeguimientoMongoRepository mongoRepository;

    @Override
    public Seguimiento save(Seguimiento seguimiento) {
        SeguimientoModel model = toModel(seguimiento);
        SeguimientoModel saved = mongoRepository.save(model);
        return toEntity(saved);
    }

    @Override
    public Optional<Seguimiento> findById(String id) {
        return mongoRepository.findById(id)
                .map(this::toEntity);
    }

    @Override
    public Page<Seguimiento> findBySeguidorId(String seguidorId, Pageable pageable) {
        return mongoRepository.findBySeguidorId(seguidorId, pageable)
                .map(this::toEntity);
    }

    @Override
    public Page<Seguimiento> findBySeguidoId(String seguidoId, Pageable pageable) {
        return mongoRepository.findBySeguidoId(seguidoId, pageable)
                .map(this::toEntity);
    }

    @Override
    public Page<Seguimiento> findBySeguidoIdAndEstado(String seguidoId, EstadoSeguimiento estado, Pageable pageable) {
        return mongoRepository.findBySeguidoIdAndEstado(seguidoId, estado.toString(), pageable)
                .map(this::toEntity);
    }

    @Override
    public Page<Seguimiento> findBySeguidorIdAndEstado(String seguidorId, EstadoSeguimiento estado, Pageable pageable) {
        return mongoRepository.findBySeguidorIdAndEstado(seguidorId, estado.toString(), pageable)
                .map(this::toEntity);
    }

    @Override
    public boolean existsBySeguidorIdAndSeguidoId(String seguidorId, String seguidoId) {
        return mongoRepository.existsBySeguidorIdAndSeguidoId(seguidorId, seguidoId);
    }

    @Override
    public boolean existsBySeguidorIdAndSeguidoIdAndEstado(String seguidorId, String seguidoId, EstadoSeguimiento estado) {
        return mongoRepository.existsBySeguidorIdAndSeguidoIdAndEstado(seguidorId, seguidoId, estado.toString());
    }

    @Override
    public void deleteBySeguidorIdAndSeguidoId(String seguidorId, String seguidoId) {
        mongoRepository.deleteBySeguidorIdAndSeguidoId(seguidorId, seguidoId);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public long countBySeguidoId(String seguidoId) {
        return mongoRepository.countBySeguidoId(seguidoId);
    }

    @Override
    public long countBySeguidorId(String seguidorId) {
        return mongoRepository.countBySeguidorId(seguidorId);
    }

    @Override
    public long countBySeguidoIdAndEstado(String seguidoId, EstadoSeguimiento estado) {
        return mongoRepository.countBySeguidoIdAndEstado(seguidoId, estado.toString());
    }

    @Override
    public long countBySeguidorIdAndEstado(String seguidorId, EstadoSeguimiento estado) {
        return mongoRepository.countBySeguidorIdAndEstado(seguidorId, estado.toString());
    }
    
    private SeguimientoModel toModel(Seguimiento entity) {
        SeguimientoModel model = new SeguimientoModel();
        model.setId(entity.getId());
        model.setSeguidorId(entity.getSeguidorId());
        model.setSeguidoId(entity.getSeguidoId());
        model.setEstado(entity.getEstado().toString());
        model.setFechaSeguimiento(entity.getFechaSeguimiento());
        model.setFechaRespuesta(entity.getFechaRespuesta());
        model.setNotificacionesActivas(entity.getNotificacionesActivas());
        return model;
    }

    private Seguimiento toEntity(SeguimientoModel model) {
        Seguimiento entity = new Seguimiento();
        entity.setId(model.getId());
        entity.setSeguidorId(model.getSeguidorId());
        entity.setSeguidoId(model.getSeguidoId());
        entity.setEstado(EstadoSeguimiento.valueOf(model.getEstado()));
        entity.setFechaSeguimiento(model.getFechaSeguimiento());
        entity.setFechaRespuesta(model.getFechaRespuesta());
        entity.setNotificacionesActivas(model.getNotificacionesActivas());
        return entity;
    }
}
