package com.comunired.categoria.graphql.resolver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.comunired.categoria.application.dto.CategoriaDTO;
import com.comunired.categoria.application.service.CategoriaService;
import com.comunired.categoria.infrastructure.entity.Categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Controller
public class CategoriaResolver {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaResolver.class);

    private final CategoriaService categoriaService;

    public CategoriaResolver(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    private CategoriaDTO toDTO(Categoria c) {
        if (c == null) return null;
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setDescripcion(c.getDescripcion());
        dto.setActivo(c.getActivo());
        return dto;
    }

    @QueryMapping(name = "listarCategorias")
    public List<CategoriaDTO> listarCategorias() {
        List<Categoria> lista = categoriaService.listarCategorias();
        return lista.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    @QueryMapping
    public CategoriaDTO obtenerCategoriaPorId(@Argument String id) {

        Optional<Categoria> opt = categoriaService.listarCategorias()
                .stream()
                .filter(c -> c != null && id != null && id.equals(c.getId()))
                .findFirst();
        return opt.map(this::toDTO).orElse(null);
    }

    @QueryMapping(name = "buscarCategoriaPorNombre")
public CategoriaDTO buscarCategoriaPorNombre(@Argument String nombre) {
    Optional<Categoria> opt = categoriaService.buscarPorNombre(nombre);
    return opt.map(this::toDTO).orElse(null);
}

    @MutationMapping
    public CategoriaDTO crearCategoria(@Argument String nombre, @Argument String descripcion, @Argument Boolean activo) {
        try {
            Categoria c = categoriaService.crearCategoria(nombre, descripcion, activo);
            return toDTO(c);
        } catch (Exception e) {
            logger.error("Error creando categoría", e);
            return null;
        }
    }

    @MutationMapping
    public CategoriaDTO actualizarCategoria(@Argument String id, @Argument String nombre, @Argument String descripcion, @Argument Boolean activo) {
        try {
            Categoria c = categoriaService.actualizarCategoria(id, nombre, descripcion, activo);
            return toDTO(c);
        } catch (Exception e) {
            logger.error("Error actualizando categoría id={}", id, e);
            return null;
        }
    }

    @MutationMapping
    public boolean eliminarCategoria(@Argument String id) {
        try {
            categoriaService.eliminarCategoria(id);
            return true;
        } catch (Exception e) {
            logger.error("Error eliminando categoría id={}", id, e);
            return false;
        }
    }

    @QueryMapping(name = "obtenerCategorias")
    public Page<CategoriaDTO> obtenerCategorias(@Argument int page, @Argument int size) {
        Page<Categoria> pageResult = categoriaService.listarCategoriasPaginado(page, size);
        List<CategoriaDTO> dtos = pageResult.getContent().stream().map(this::toDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageResult.getPageable(), pageResult.getTotalElements());
    }
    
}