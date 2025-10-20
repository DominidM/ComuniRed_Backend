package com.comunired.categoria.graphql.resolver;

import com.comunired.categoria.application.dto.CategoriaDTO;
import com.comunired.categoria.application.service.CategoriaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoriaResolver {

    private final CategoriaService service;

    public CategoriaResolver(CategoriaService service) {
        this.service = service;
    }

    @QueryMapping
    public List<CategoriaDTO> listarCategorias() {
        return service.listarCategorias();
    }

    @QueryMapping
    public CategoriaDTO buscarCategoriaPorNombre(@Argument String nombre) {
        Optional<CategoriaDTO> categoria = service.buscarPorNombre(nombre);
        return categoria.orElse(null);
    }

    @MutationMapping
    public CategoriaDTO crearCategoria(
            @Argument String nombre,
            @Argument String descripcion,
            @Argument boolean activo
    ) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);
        dto.setActivo(activo);
        return service.crearCategoria(dto);
    }

    @MutationMapping
    public CategoriaDTO actualizarCategoria(
            @Argument String nombre,
            @Argument String descripcion,
            @Argument boolean activo
    ) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);
        dto.setActivo(activo);
        return service.actualizarCategoria(nombre, dto).orElse(null);
    }

    @MutationMapping
    public String eliminarCategoria(@Argument String nombre) {
        boolean eliminado = service.eliminarCategoria(nombre);
        return eliminado ? "Categoría eliminada correctamente" : "Categoría no encontrada";
    }
}
