package com.comunired.mensajeria.graphql.resolver;

import com.comunired.mensajeria.application.dto.MensajeDTO;
import com.comunired.mensajeria.application.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MensajeResolver {
    
    @Autowired
    private MensajeService mensajeService;

    
    @QueryMapping
    public Map<String, Object> mensajesDeConversacion(@Argument String conversacionId,
                                                       @Argument int page,
                                                       @Argument int size) {
        Page<MensajeDTO> resultado = mensajeService.obtenerMensajes(conversacionId, page, size);
        return createPageResponse(resultado);
    }

    @QueryMapping
    public Integer contarMensajesNoLeidos(@Argument String conversacionId,
                                           @Argument String usuarioId) {
        return (int) mensajeService.contarNoLeidos(conversacionId, usuarioId);
    }

    
    @MutationMapping
    public MensajeDTO enviarMensaje(@Argument String conversacionId,
                                     @Argument String emisorId,
                                     @Argument String contenido) {
        return mensajeService.enviarMensaje(conversacionId, emisorId, contenido);
    }

    @MutationMapping
    public Boolean marcarMensajesComoLeidos(@Argument String conversacionId,
                                             @Argument String usuarioId) {
        return mensajeService.marcarComoLeidos(conversacionId, usuarioId);
    }

    
    private Map<String, Object> createPageResponse(Page<MensajeDTO> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("number", page.getNumber());
        response.put("size", page.getSize());
        return response;
    }
}
