package com.comunired.tipos_reaccion.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion;
import com.comunired.tipos_reaccion.domain.repository.Tipos_reaccionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public class Tipos_reaccionRepositoryImpl implements Tipos_reaccionRepository {

    private final Tipos_reaccionMongoRepository mongoRepository;

    public Tipos_reaccionRepositoryImpl(Tipos_reaccionMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Tipos_reaccion guardar(Tipos_reaccion tipo) {
        return mongoRepository.save(tipo);
    }

    @Override
    public Tipos_reaccion modificar(Tipos_reaccion tipo) {
        return mongoRepository.save(tipo);
    }

    @Override
    public Optional<Tipos_reaccion> buscarPorId(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<Tipos_reaccion> buscarPorLabel(String label) {
        return mongoRepository.findByLabel(label);
    }

    @Override
    public List<Tipos_reaccion> listar() {
        return mongoRepository.findAll();
    }

    @Override
    public void eliminar(String id) {
        mongoRepository.deleteById(id);
    }
    
    @Override
    public Page<Tipos_reaccion> listarPaginado(Pageable pageable) {
        return mongoRepository.findAll(pageable);
    }
}
