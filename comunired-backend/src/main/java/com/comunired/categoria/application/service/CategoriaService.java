package com.comunired.categoria.application.service;

import com.comunired.categoria.domain.entity.Categoria;
import com.comunired.categoria.domain.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.listar();
    }

    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.buscarPorNombre(nombre);
    }

    public Categoria crearCategoria(String nombre, String descripcion, Boolean activo) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        categoria.setActivo(activo);
        return categoriaRepository.guardar(categoria);
    }

    public Categoria actualizarCategoria(String id, String nombre, String descripcion, Boolean activo) {
        Optional<Categoria> categoriaOpt = categoriaRepository.buscarPorId(id);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            categoria.setActivo(activo);
            return categoriaRepository.modificar(categoria);
        } else {
            throw new RuntimeException("Categoría no encontrada con id: " + id);
        }
    }

    public void eliminarCategoria(String id) {
        categoriaRepository.eliminar(id);
    }
}
