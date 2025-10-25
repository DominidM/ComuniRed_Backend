package com.comunired.categoria.domain.ports;

import java.util.List;
import java.util.Optional;

import com.comunired.categoria.infrastructure.entity.Categoria;

public interface CategoriaRepository {

    Categoria guardar(Categoria categoria);

    Categoria modificar(Categoria categoria);

    Optional<Categoria> buscarPorId(String id);
    
    Optional<Categoria> buscarPorNombre(String nombre);

    List<Categoria> listar();

    void eliminar(String id);
}