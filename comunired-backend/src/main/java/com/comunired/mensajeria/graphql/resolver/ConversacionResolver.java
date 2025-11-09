package com.comunired.mensajeria.graphql.resolver;

import com.comunired.mensajeria.application.dto.ConversacionDTO;
import com.comunired.mensajeria.application.service.ConversacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ConversacionResolver {
    
    @Autowired
    private ConversacionService conversacionService;

    // ========== QUERIES ==========
    
    @QueryMapping
    public Map<String, Object> misConversaciones(@Argument String usuarioId,
                                                  @Argument int page,
                                                  @Argument int size) {
        Page<ConversacionDTO> resultado = conversacionService.obtenerMisConversaciones(usuarioId, page, size);
        return createPageResponse(resultado);
    }

    @QueryMapping
    public ConversacionDTO obtenerConversacion(@Argument String conversacionId) {
        return conversacionService.obtenerConversacion(conversacionId);
    }

    @QueryMapping
    public ConversacionDTO buscarConversacionConUsuario(@Argument String usuarioId,
                                                         @Argument String otroUsuarioId) {
        return conversacionService.buscarConversacionConUsuario(usuarioId, otroUsuarioId)
                .orElse(null);
    }

    // ========== MUTATIONS ==========
    
    @MutationMapping
    public ConversacionDTO crearConversacion(@Argument String usuarioId,
                                              @Argument String otroUsuarioId) {
        return conversacionService.crearConversacion(usuarioId, otroUsuarioId);
    }

    @MutationMapping
    public Boolean eliminarConversacion(@Argument String conversacionId,
                                         @Argument String usuarioId) {
        return conversacionService.eliminarConversacion(conversacionId, usuarioId);
    }

    // ========== HELPER METHODS ==========
    
    private Map<String, Object> createPageResponse(Page<ConversacionDTO> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("number", page.getNumber());
        response.put("size", page.getSize());
        return response;
    }
}
