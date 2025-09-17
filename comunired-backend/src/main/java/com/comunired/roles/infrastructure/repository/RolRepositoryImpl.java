package com.comunired.roles.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.comunired.roles.domain.entity.Rol;
import com.comunired.roles.domain.repository.RolRepository;

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
    public Rol save(Rol rol) {
        return rolMongoRepository.save(rol);
    }

    @Override
    public Rol findByNombre(String nombre) {
        return rolMongoRepository.findByNombre(nombre);
    }
}