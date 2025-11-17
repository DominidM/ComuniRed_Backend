package com.comunired.comentarios.graphql.resolver;

import com.comunired.comentarios.application.service.ComentariosService;
import com.comunired.comentarios.application.dto.ComentariosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class ComentariosResolver {

    @Autowired
    private ComentariosService comentariosService;

    @QueryMapping
    public List<ComentariosDTO> comentariosPorQueja(@Argument String quejaId) {
        return comentariosService.findByQuejaId(quejaId);
    }

    @QueryMapping
    public List<ComentariosDTO> comentariosPorUsuario(@Argument String usuarioId) {
        return comentariosService.findByUsuarioId(usuarioId);
    }

    @QueryMapping
    public Long contarComentarios(@Argument String quejaId) {
        return comentariosService.countByQuejaId(quejaId);
    }

    @QueryMapping
    public ComentariosDTO buscarComentario(@Argument String id) {
        return comentariosService.findById(id);
    }

    @QueryMapping
    public List<ComentariosDTO> buscarComentariosPorTexto(@Argument String texto, @Argument String usuarioId) {
        return comentariosService.searchByText(texto, usuarioId);
    }

    @MutationMapping
    public ComentariosDTO agregarComentario(@Argument String quejaId, 
                                           @Argument String usuarioId, 
                                           @Argument String texto) {
        return comentariosService.create(quejaId, usuarioId, texto);
    }

    @MutationMapping
    public ComentariosDTO editarComentario(@Argument String id, 
                                          @Argument String usuarioId, 
                                          @Argument String texto) {
        return comentariosService.update(id, usuarioId, texto);
    }

    @MutationMapping
    public Boolean eliminarComentario(@Argument String id, @Argument String usuarioId) {
        return comentariosService.delete(id, usuarioId);
    }

    @QueryMapping
    public List<ComentariosDTO> buscarComentariosPorUsuario(@Argument String usuarioId) {
        return comentariosService.buscarComentariosPorUsuarioId(usuarioId);
    }
}
