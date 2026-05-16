package com.comunired.notificaciones.graphql.subscription;

import com.comunired.notificaciones.application.dto.NotificacionDTO;
import com.comunired.notificaciones.application.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class NotificacionSubscription {

    @Autowired
    private NotificacionService notificacionService;

    @SubscriptionMapping
    public Flux<NotificacionDTO> nuevaNotificacion(@Argument("usuario_id") String usuarioId) {
        return notificacionService.suscribirseANotificaciones(usuarioId);
    }
}
