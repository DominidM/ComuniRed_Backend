package com.comunired.mensajeria.application.service;

import com.comunired.mensajeria.application.dto.MensajeDTO;
import com.comunired.mensajeria.application.dto.MensajeEvent;
import com.comunired.mensajeria.domain.entity.Conversacion;
import com.comunired.mensajeria.domain.entity.Mensaje;
import com.comunired.mensajeria.domain.repository.ConversacionRepository;
import com.comunired.mensajeria.domain.repository.MensajeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.util.Optional;

@Service
public class MensajeService {

    private static final Logger log = LoggerFactory.getLogger(MensajeService.class);

    private final Sinks.Many<MensajeEvent> mensajeSink = Sinks.many().multicast().onBackpressureBuffer(1024);
    private final Sinks.Many<MensajeDTO> lecturaSink = Sinks.many().multicast().onBackpressureBuffer(1024);

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private ConversacionRepository conversacionRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public MensajeDTO enviarMensaje(String conversacionId, String emisorId, String contenido) {
        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));

        if (!conversacion.esParticipante(emisorId)) {
            throw new IllegalStateException("No eres parte de esta conversación");
        }

        Mensaje mensaje = new Mensaje(conversacionId, emisorId, contenido);
        Mensaje saved = mensajeRepository.save(mensaje);

        conversacion.setUltimoMensajeId(saved.getId());
        conversacion.setFechaUltimaActividad(Instant.now());
        conversacionRepository.save(conversacion);

        MensajeDTO savedDTO = toDTO(saved);

        String destinatario = conversacion.getOtroParticipante(emisorId);
        if (destinatario != null) {
            MensajeEvent event = new MensajeEvent(savedDTO, destinatario);
            Sinks.EmitResult result = mensajeSink.tryEmitNext(event);
            if (result.isFailure()) {
                log.warn("mensajeSink emit result: {}", result);
            }
            eventPublisher.publishEvent(event);
        }

        return savedDTO;
    }

    public Page<MensajeDTO> obtenerMensajes(String conversacionId, int page, int size) {
        return mensajeRepository.findByConversacionId(conversacionId,
                PageRequest.of(page, size))
                .map(this::toDTO);
    }

    public Optional<MensajeDTO> obtenerMensaje(String mensajeId) {
        return mensajeRepository.findById(mensajeId)
                .map(this::toDTO);
    }

    @Transactional
    public boolean marcarComoLeidos(String conversacionId, String usuarioId) {
        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));

        if (!conversacion.esParticipante(usuarioId)) {
            throw new IllegalStateException("No eres parte de esta conversación");
        }

        mensajeRepository.marcarComoLeidos(conversacionId, usuarioId);

        MensajeDTO dto = new MensajeDTO();
        dto.setConversacionId(conversacionId);
        dto.setLeido(true);
        dto.setFechaLectura(Instant.now());
        Sinks.EmitResult result = lecturaSink.tryEmitNext(dto);
        if (result.isFailure()) {
            log.warn("lecturaSink emit result: {}", result);
        }

        return true;
    }

    public long contarNoLeidos(String conversacionId, String usuarioId) {
        return mensajeRepository.countNoLeidosByConversacionAndReceptor(conversacionId, usuarioId);
    }

    public Flux<MensajeDTO> suscribirseAMensajes(String usuarioId) {
        return mensajeSink.asFlux()
                .filter(event -> event.getDestinatarioId().equals(usuarioId))
                .map(MensajeEvent::getMensaje);
    }

    public Flux<MensajeDTO> suscribirseALecturas(String conversacionId) {
        return lecturaSink.asFlux()
                .filter(dto -> dto.getConversacionId().equals(conversacionId));
    }

    private MensajeDTO toDTO(Mensaje entity) {
        MensajeDTO dto = new MensajeDTO();
        dto.setId(entity.getId());
        dto.setConversacionId(entity.getConversacionId());
        dto.setEmisorId(entity.getEmisorId());
        dto.setContenido(entity.getContenido());
        dto.setFechaEnvio(entity.getFechaEnvio());
        dto.setLeido(entity.getLeido());
        dto.setFechaLectura(entity.getFechaLectura());
        return dto;
    }
}
