package com.comunired.mensajeria.application.service;

import com.comunired.mensajeria.application.dto.MensajeDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class MensajeService {
    
    private final Sinks.Many<MensajeDTO> mensajeSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<MensajeDTO> lecturaSink = Sinks.many().multicast().onBackpressureBuffer();

    public Flux<MensajeDTO> suscribirseAMensajes(String usuarioId) {
        return mensajeSink.asFlux()
            .filter(mensaje -> mensaje.getEmisorId().equals(usuarioId));
    }

    public Flux<MensajeDTO> suscribirseALecturas(String conversacionId) {
        return lecturaSink.asFlux()
            .filter(mensaje -> mensaje.getConversacionId().equals(conversacionId));
    }
}
