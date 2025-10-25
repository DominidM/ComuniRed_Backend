package com.comunired.roles.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.comunired.roles.application.dto.RolDTO;
import com.comunired.roles.application.port.out.RolRepository;
import com.comunired.roles.infrastructure.entity.RolEntity;
import com.comunired.roles.infrastructure.mapper.RolMapper;

/**
 * Adapter que implementa el port de la capa application (DTO + Pageable)
 * y delega al RolMongoRepository de Spring Data, usando RolMapper para conversiones.
 */
@Component
public class RolRepositoryDtoAdapter implements RolRepository {

    private final RolMongoRepository mongoRepo;
    private final RolMapper mapper;

    public RolRepositoryDtoAdapter(RolMongoRepository mongoRepo, RolMapper mapper) {
        this.mongoRepo = mongoRepo;
        this.mapper = mapper;
    }

    @Override
    public List<RolDTO> findAll() {
        return mongoRepo.findAll().stream()
                .map(e -> mapper.toDTO(mapper.toDomain(e)))
                .collect(Collectors.toList());
    }

    @Override
    public Page<RolDTO> findAll(Pageable pageable) {
        Page<RolEntity> page = mongoRepo.findAll(pageable);
        Page<RolDTO> dtoPage = page.map(e -> mapper.toDTO(mapper.toDomain(e)));
        return dtoPage;
    }

    @Override
    public RolDTO save(RolDTO rolDto) {
        // dto -> domain -> entity -> save -> domain -> dto
        var domain = mapper.fromDTO(rolDto);
        var entity = mapper.toEntity(domain);
        var saved = mongoRepo.save(entity);
        return mapper.toDTO(mapper.toDomain(saved));
    }

    @Override
    public RolDTO findByNombre(String nombre) {
        return mongoRepo.findByNombre(nombre)
                .map(e -> mapper.toDTO(mapper.toDomain(e)))
                .orElse(null);
    }

    @Override
    public Optional<RolDTO> findById(String id) {
        return mongoRepo.findById(id).map(e -> mapper.toDTO(mapper.toDomain(e)));
    }

    @Override
    public boolean existsById(String id) {
        return mongoRepo.existsById(id);
    }

    @Override
    public void deleteById(String id) {
        mongoRepo.deleteById(id);
    }
}