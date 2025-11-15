package com.comunired.reacciones.graphql.resolver;

import com.comunired.reacciones.application.service.ReaccionesService;
import com.comunired.reacciones.application.dto.ReaccionesDTO;
import com.comunired.usuarios.application.dto.UsuariosDTO;
import com.comunired.quejas.application.dto.QuejasDTO.ReactionsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ReaccionesResolver {

    @Autowired
    private ReaccionesService reaccionesService;

    @QueryMapping
    public List<ReaccionesDTO> reaccionesPorQueja(@Argument String quejaId) {
        return reaccionesService.findByQuejaId(quejaId);
    }

    @QueryMapping
    public List<UsuariosDTO> usuariosPorReaccion(@Argument String quejaId, @Argument String tipoReaccion) {
        return reaccionesService.getUsersByReactionType(quejaId, tipoReaccion);
    }

    @MutationMapping
    public ReactionsDTO toggleReaccion(@Argument String quejaId, 
                                       @Argument String tipoReaccion, 
                                       @Argument String usuarioId) {
        return reaccionesService.toggleReaction(quejaId, tipoReaccion, usuarioId);
    }
}
