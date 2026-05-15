package com.comunired.reels.infrastructure.adapter.out.persistence;

import com.comunired.reels.application.port.out.ReelRepositoryPort;
import com.comunired.reels.domain.entity.Reel;
import com.comunired.reels.infrastructure.mapper.ReelInfraMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReelMongoAdapter implements ReelRepositoryPort {

    private final ReelMongoRepository mongoRepository;
    private final ReelInfraMapper mapper;

    @Override
    public Reel guardar(Reel reel) {
        ReelDocument saved = mongoRepository.save(mapper.toDocument(reel));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Reel> buscarPorId(String id) {
        return mongoRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Reel> buscarActivos() {
        return mongoRepository.findByActivoTrue()
            .stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public List<Reel> buscarTodos() {
        return mongoRepository.findAll()
            .stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public List<Reel> buscarPorTermino(String termino) {
        return mongoRepository
            .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                termino, termino, termino)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public void eliminar(String id) {
        mongoRepository.deleteById(id);
    }
}
