package com.comunired.categoria.infrastructure.repository;

import com.comunired.categoria.domain.ports.CategoriaRepository;
import com.comunired.categoria.infrastructure.entity.Categoria;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public class CategoriaRepositoryImpl implements CategoriaRepository {

    private final CategoriaMongoRepository mongoRepository;

    public CategoriaRepositoryImpl(CategoriaMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        return mongoRepository.save(categoria);
    }

    @Override
    public Categoria modificar(Categoria categoria) {
        return mongoRepository.save(categoria);
    }

    @Override
    public Optional<Categoria> buscarPorId(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return mongoRepository.findByNombre(nombre);
    }

    @Override
    public List<Categoria> listar() {
        return mongoRepository.findAll();
    }

    @Override
    public Page<Categoria> listarPaginado(Pageable pageable) {
        return mongoRepository.findAll(pageable);
    }

    @Override
    public void eliminar(String id) {
        mongoRepository.deleteById(id);
    }
}
