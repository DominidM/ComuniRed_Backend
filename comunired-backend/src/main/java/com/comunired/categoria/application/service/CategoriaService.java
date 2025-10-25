package com.comunired.categoria.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.comunired.categoria.infrastructure.entity.Categoria;
import com.comunired.categoria.infrastructure.repository.CategoriaMongoRepository;

@Service
public class CategoriaService {

    private final CategoriaMongoRepository categoriaRepository;

    public CategoriaService(CategoriaMongoRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Lista todas las categorías (antes listar())
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    // Buscar por nombre (devuelve Optional)
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    // Crear categoría
    public Categoria crearCategoria(String nombre, String descripcion, Boolean activo) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        categoria.setActivo(activo != null ? activo : Boolean.TRUE);
        return categoriaRepository.save(categoria);
    }

    // Actualizar categoría: busca por id y guarda
    public Categoria actualizarCategoria(String id, String nombre, String descripcion, Boolean activo) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            categoria.setActivo(activo != null ? activo : categoria.getActivo());
            return categoriaRepository.save(categoria);
        } else {
            throw new RuntimeException("Categoría no encontrada con id: " + id);
        }
    }

    // Eliminar por id
    public void eliminarCategoria(String id) {
        categoriaRepository.deleteById(id);
    }
}