package com.comunired.categoria.domain.repository;

import java.util.List;
import java.util.Optional;

import com.comunired.categoria.domain.entity.Categoria;

public interface CategoriaRepository {

    Categoria guardar(Categoria categoria);

    Categoria modificar(Categoria categoria);

    Optional<Categoria> buscarPorId(String id);
    
    Optional<Categoria> buscarPorNombre(String nombre);

    List<Categoria> listar();

    void eliminar(String id);
}