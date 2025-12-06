package com.comunired.quejas.infrastructure.repository;

import com.comunired.quejas.domain.entity.Quejas;
import com.comunired.quejas.domain.repository.QuejasRepository;
import com.comunired.quejas.infrastructure.model.QuejasModel;
import com.comunired.reacciones.infrastructure.repository.ReaccionesMongoRepository;
import com.comunired.tipos_reaccion.domain.repository.Tipos_reaccionRepository;
import com.comunired.estados_queja.domain.repository.Estados_quejaRepository;
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

    @Autowired
    private ReaccionesMongoRepository reaccionesMongoRepository;

    @Autowired
    private Tipos_reaccionRepository tiposReaccionRepository;

    @Autowired
    private Estados_quejaRepository estadosRepository;

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

    @Override
    public List<Quejas> findQuejasAprobadas() {
        String acceptId = tiposReaccionRepository.buscarPorKey("accept")
                .map(t -> t.getId())
                .orElse(null);
        
        if (acceptId == null) return List.of();
        
        return mongoRepository.findAll()
                .stream()
                .filter(queja -> {
                    long acceptCount = reaccionesMongoRepository
                            .countByQuejaIdAndTipoReaccionId(queja.getId(), acceptId);
                    return acceptCount >= 5;
                })
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quejas> findQuejasParaRevisar() {
        String estadoAprobadaId = estadosRepository.listar().stream()
                .filter(e -> "APROBADA".equalsIgnoreCase(e.getClave()))
                .findFirst()
                .map(e -> e.getId())
                .orElse(null);
        
        if (estadoAprobadaId == null) return List.of();
        
        return mongoRepository.findByEstadoId(estadoAprobadaId)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quejas> findByNivelRiesgo(String nivelRiesgo) {
        return mongoRepository.findByNivelRiesgo(nivelRiesgo)
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
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
        
        model.setNivel_riesgo(entity.getNivel_riesgo());
        model.setFecha_clasificacion(entity.getFecha_clasificacion());
        model.setClasificado_por_id(entity.getClasificado_por_id());
        model.setFecha_aprobacion(entity.getFecha_aprobacion());
        
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
        
        entity.setNivel_riesgo(model.getNivel_riesgo());
        entity.setFecha_clasificacion(model.getFecha_clasificacion());
        entity.setClasificado_por_id(model.getClasificado_por_id());
        entity.setFecha_aprobacion(model.getFecha_aprobacion());
        
        return entity;
    }
}
