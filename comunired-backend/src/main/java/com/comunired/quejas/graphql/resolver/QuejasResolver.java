package com.comunired.quejas.graphql.resolver;

import com.comunired.quejas.application.service.QuejasService;
import com.comunired.quejas.application.dto.QuejasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class QuejasResolver {

    @Autowired
    private QuejasService quejasService;

    // ========== QUERIES ==========

    @QueryMapping
    public List<QuejasDTO> obtenerQuejas(@Argument String usuarioActualId) {
        return quejasService.findAll(usuarioActualId);
    }

    @QueryMapping
    public QuejasDTO obtenerQuejaPorId(@Argument String id, @Argument String usuarioActualId) {
        return quejasService.findById(id, usuarioActualId);
    }

    @QueryMapping
    public List<QuejasDTO> quejasPorUsuario(@Argument String usuarioId, @Argument String usuarioActualId) {
        return quejasService.findByUsuarioId(usuarioId, usuarioActualId);
    }


    @MutationMapping
    public QuejasDTO crearQueja(
        @Argument String titulo,
        @Argument String descripcion,
        @Argument String categoriaId,
        @Argument String ubicacion,
        @Argument MultipartFile imagen,
        @Argument String usuarioId
    ) {
        System.out.println("📥 CREAR QUEJA");
        System.out.println("  - Título: " + titulo);
        System.out.println("  - Usuario: " + usuarioId);
        
        // ✅ NO PASAR imagen al service
        return quejasService.create(titulo, descripcion, categoriaId, ubicacion, usuarioId);
    }


    @MutationMapping
    public QuejasDTO actualizarQueja(
        @Argument String id,
        @Argument String titulo,
        @Argument String descripcion,
        @Argument String categoriaId,
        @Argument String estadoId,
        @Argument String ubicacion,
        @Argument String imagen_url
    ) {
        return quejasService.update(id, titulo, descripcion, categoriaId, estadoId, ubicacion, imagen_url);
    }


    @MutationMapping
    public Boolean eliminarQueja(@Argument String id) {
        return quejasService.delete(id);
    }
}
