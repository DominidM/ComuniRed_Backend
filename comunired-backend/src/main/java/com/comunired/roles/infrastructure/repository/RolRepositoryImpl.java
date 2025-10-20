package com.comunired.roles.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.comunired.roles.domain.entity.Rol;
import com.comunired.roles.domain.repository.RolRepository;

/**
 * Implementación que delega en Spring Data MongoRepository.
 */
@Repository
public class RolRepositoryImpl implements RolRepository {
    private final RolMongoRepository rolMongoRepository;

    public RolRepositoryImpl(RolMongoRepository rolMongoRepository) {
        this.rolMongoRepository = rolMongoRepository;
    }

    @Override
    public List<Rol> findAll() {
        return rolMongoRepository.findAll();
    }

    @Override
    public Page<Rol> findAll(Pageable pageable) {
        return rolMongoRepository.findAll(pageable);
    }

    @Override
    public Rol save(Rol rol) {
        return rolMongoRepository.save(rol);
    }

    @Override
    public Rol findByNombre(String nombre) {
        return rolMongoRepository.findByNombre(nombre);
    }

    @Override
    public Optional<Rol> findById(String id) {
        return rolMongoRepository.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return rolMongoRepository.existsById(id);
    }

    @Override
    public void deleteById(String id) {
        rolMongoRepository.deleteById(id);
    }
}