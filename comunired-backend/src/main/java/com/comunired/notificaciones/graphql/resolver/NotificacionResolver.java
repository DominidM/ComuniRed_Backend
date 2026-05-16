package com.comunired.notificaciones.graphql.resolver;

import com.comunired.notificaciones.application.dto.NotificacionDTO;
import com.comunired.notificaciones.application.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class NotificacionResolver {

    @Autowired
    private NotificacionService notificacionService;

    @QueryMapping
    public Map<String, Object> misNotificaciones(@Argument String usuarioId,
                                                  @Argument int page,
                                                  @Argument int size) {
        Page<NotificacionDTO> resultado = notificacionService.obtenerNotificaciones(usuarioId, page, size);
        return createPageResponse(resultado);
    }

    @QueryMapping
    public Integer contarNotificacionesNoLeidas(@Argument String usuarioId) {
        return (int) notificacionService.contarNoLeidas(usuarioId);
    }

    @MutationMapping
    public NotificacionDTO marcarNotificacionLeida(@Argument String id) {
        return notificacionService.marcarLeida(id);
    }

    @MutationMapping
    public Boolean marcarTodasNotificacionesLeidas(@Argument String usuarioId) {
        return notificacionService.marcarTodasLeidas(usuarioId);
    }

    private Map<String, Object> createPageResponse(Page<NotificacionDTO> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("number", page.getNumber());
        response.put("size", page.getSize());
        return response;
    }
}
