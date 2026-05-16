package com.comunired.quejas.infrastructure.adapter.out.persistence;

import com.comunired.quejas.application.port.out.QuejaOutPorts.QuejaRepositoryPort;
import com.comunired.quejas.application.port.out.QuejaOutPorts.EstadoQuejaPort;
import com.comunired.quejas.domain.entity.Queja;
import com.comunired.quejas.infrastructure.mapper.QuejaInfraMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    Page<QuejaDocument> findByEstadoIdIn(List<String> estadoIds, Pageable pageable);

    List<QuejaDocument> findByEstadoIdIn(List<String> estadoIds);
}

@Component
public class QuejaMongoAdapter implements QuejaRepositoryPort {

    private final QuejasMongoRepository mongo;
    private final QuejaInfraMapper mapper;
    private final EstadoQuejaPort estadoPort;

    public QuejaMongoAdapter(QuejasMongoRepository mongo,
            QuejaInfraMapper mapper,
            EstadoQuejaPort estadoPort) {
        this.mongo = mongo;
        this.mapper = mapper;
        this.estadoPort = estadoPort;
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
        return mongo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Queja> buscarPorUsuarioId(String usuarioId) {
        return mongo.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Queja> buscarAprobadas() {
        List<String> estadoIds = List.of("aprobado", "publicado")
                .stream()
                .map(clave -> estadoPort.buscarPorClave(clave))
                .filter(Optional::isPresent)
                .map(opt -> opt.get().id())
                .toList();
        if (estadoIds.isEmpty()) return List.of();
        return mongo.findByEstadoIdIn(estadoIds).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Queja> buscarParaRevisar() {
        List<String> estadoIds = List.of("pendiente", "aprobacion")
                .stream()
                .map(clave -> estadoPort.buscarPorClave(clave))
                .filter(Optional::isPresent)
                .map(opt -> opt.get().id())
                .toList();
        if (estadoIds.isEmpty()) return List.of();
        return mongo.findByEstadoIdIn(estadoIds).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existePorId(String id) {
        return mongo.existsById(id);
    }

    @Override
    public void eliminar(String id) {
        mongo.deleteById(id);
    }

    @Override
    public Page<Queja> buscarTodasPaginado(org.springframework.data.domain.Pageable pageable) {
        return mongo.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Queja> buscarPorEstadosClavePaginado(List<String> claves, int page, int size) {
        // Resuelve claves → IDs usando buscarPorClave (lo que sí existe en el port)
        List<String> estadoIds = claves.stream()
                .map(clave -> estadoPort.buscarPorClave(clave))
                .filter(Optional::isPresent)
                .map(opt -> opt.get().id())
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "fechaCreacion"));

        return mongo.findByEstadoIdIn(estadoIds, pageable)
                .map(mapper::toDomain);
    }
}
