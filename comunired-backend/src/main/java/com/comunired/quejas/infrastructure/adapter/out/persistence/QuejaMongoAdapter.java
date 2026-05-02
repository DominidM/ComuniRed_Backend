package com.comunired.quejas.infrastructure.adapter.out.persistence;

import com.comunired.quejas.application.port.out.QuejaOutPorts.QuejaRepositoryPort;
import com.comunired.quejas.domain.entity.Queja;
import com.comunired.quejas.infrastructure.mapper.QuejaInfraMapper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
interface QuejasMongoRepository extends MongoRepository<QuejaDocument, String> {
    List<QuejaDocument> findByUsuarioId(String usuarioId);
    List<QuejaDocument> findByEstadoId(String estadoId);
}

@Component
public class QuejaMongoAdapter implements QuejaRepositoryPort {

    private final QuejasMongoRepository mongo;
    private final QuejaInfraMapper mapper;

    public QuejaMongoAdapter(QuejasMongoRepository mongo, QuejaInfraMapper mapper) {
        this.mongo = mongo;
        this.mapper = mapper;
    }

    @Override
    public Queja guardar(Queja queja) {
        return mapper.toDomain(mongo.save(mapper.toDocument(queja)));
    }

    @Override
    public Optional<Queja> buscarPorId(String id) {
        return mongo.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Queja> buscarTodas() {
        return mongo.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Queja> buscarPorUsuarioId(String usuarioId) {
        return mongo.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Queja> buscarAprobadas() {
        return mongo.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Queja> buscarParaRevisar() {
        return mongo.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existePorId(String id) {
        return mongo.existsById(id);
    }

    @Override
    public void eliminar(String id) {
        mongo.deleteById(id);
    }
}