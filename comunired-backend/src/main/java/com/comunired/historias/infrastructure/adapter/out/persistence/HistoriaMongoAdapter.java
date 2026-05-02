package com.comunired.historias.infrastructure.adapter.out.persistence;

import com.comunired.historias.application.port.out.HistoriaRepositoryPort;
import com.comunired.historias.domain.entity.Historia;
import com.comunired.historias.infrastructure.mapper.HistoriaInfraMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HistoriaMongoAdapter implements HistoriaRepositoryPort {

    private final HistoriaMongoRepository mongoRepository;
    private final HistoriaInfraMapper mapper;

    @Override
    public Historia guardar(Historia historia) {
        HistoriaDocument saved = mongoRepository.save(mapper.toDocument(historia));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Historia> buscarPorId(String id) {
        return mongoRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Historia> buscarActivas() {
        return mongoRepository.findByActivaTrue()
            .stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public void guardarActualizada(Historia historia) {
        mongoRepository.save(mapper.toDocument(historia));
    }
}