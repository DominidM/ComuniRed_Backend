package com.comunired.reacciones.infrastructure.repository;

import com.comunired.reacciones.domain.entity.Reacciones;
import com.comunired.reacciones.domain.repository.ReaccionesRepository;
import com.comunired.reacciones.infrastructure.model.ReaccionesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReaccionesRepositoryImpl implements ReaccionesRepository {

    @Autowired
    private ReaccionesMongoRepository mongoRepository;

    @Override
    public Reacciones save(Reacciones reaccion) {
        ReaccionesModel model = toModel(reaccion);
        ReaccionesModel saved = mongoRepository.save(model);
        return toEntity(saved);
    }

    @Override
    public Optional<Reacciones> findById(String id) {
        return mongoRepository.findById(id).map(this::toEntity);
    }

    @Override
    public List<Reacciones> findByQuejaId(String quejaId) {
        return mongoRepository.findByQuejaId(quejaId)  // ✅ CORREGIDO
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reacciones> findByUsuarioId(String usuarioId) {
        return mongoRepository.findByUsuarioId(usuarioId)  // ✅ CORREGIDO
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Reacciones> findByQuejaIdAndUsuarioId(String quejaId, String usuarioId) {
        return mongoRepository.findByQuejaIdAndUsuarioId(quejaId, usuarioId)  // ✅ CORREGIDO
                .map(this::toEntity);
    }

    @Override
    public Optional<Reacciones> findByQuejaIdAndUsuarioIdAndTipoReaccionId(String quejaId, String usuarioId, String tipoReaccionId) {
        return mongoRepository.findByQuejaIdAndUsuarioIdAndTipoReaccionId(quejaId, usuarioId, tipoReaccionId)  // ✅ CORREGIDO
                .map(this::toEntity);
    }

    @Override
    public long countByQuejaIdAndTipoReaccionId(String quejaId, String tipoReaccionId) {
        return mongoRepository.countByQuejaIdAndTipoReaccionId(quejaId, tipoReaccionId);  // ✅ CORREGIDO
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void delete(Reacciones reaccion) {
        if (reaccion.getId() != null) {
            mongoRepository.deleteById(reaccion.getId());
        }
    }

    @Override
    public boolean existsById(String id) {
        return mongoRepository.existsById(id);
    }

    private ReaccionesModel toModel(Reacciones entity) {
        ReaccionesModel model = new ReaccionesModel();
        model.setId(entity.getId());
        model.setQueja_id(entity.getQueja_id());
        model.setUsuario_id(entity.getUsuario_id());
        model.setTipo_reaccion_id(entity.getTipo_reaccion_id());
        model.setFecha_reaccion(entity.getFecha_reaccion());
        return model;
    }

    private Reacciones toEntity(ReaccionesModel model) {
        Reacciones entity = new Reacciones();
        entity.setId(model.getId());
        entity.setQueja_id(model.getQueja_id());
        entity.setUsuario_id(model.getUsuario_id());
        entity.setTipo_reaccion_id(model.getTipo_reaccion_id());
        entity.setFecha_reaccion(model.getFecha_reaccion());
        return entity;
    }
}
