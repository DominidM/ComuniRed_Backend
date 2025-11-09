package com.comunired.mensajeria.graphql.subscription;

import com.comunired.mensajeria.application.dto.MensajeDTO;
import com.comunired.mensajeria.application.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class MensajeSubscription {
    
    @Autowired
    private MensajeService mensajeService;

    @SubscriptionMapping
    public Flux<MensajeDTO> nuevoMensajeRecibido(@Argument("usuario_id") String usuarioId) {
        return mensajeService.suscribirseAMensajes(usuarioId);
    }

    @SubscriptionMapping
    public Flux<MensajeDTO> mensajeLeido(@Argument("conversacion_id") String conversacionId) {
        return mensajeService.suscribirseALecturas(conversacionId);
    }
}
