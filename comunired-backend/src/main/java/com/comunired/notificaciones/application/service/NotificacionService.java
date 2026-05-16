package com.comunired.notificaciones.application.service;

import com.comunired.mensajeria.application.dto.MensajeEvent;
import com.comunired.notificaciones.application.dto.NotificacionDTO;
import com.comunired.notificaciones.domain.entity.Notificacion;
import com.comunired.notificaciones.domain.repository.NotificacionRepository;
import com.comunired.quejas.application.port.out.QuejaOutPorts.QuejaRepositoryPort;
import com.comunired.quejas.domain.event.QuejaEvents.QuejaClasificada;
import com.comunired.quejas.domain.event.QuejaEvents.QuejaEstadoCambiado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final Sinks.Many<NotificacionDTO> notificacionSink = Sinks.many().multicast().onBackpressureBuffer(1024);

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private QuejaRepositoryPort quejaRepository;

    public Page<NotificacionDTO> obtenerNotificaciones(String usuarioId, int page, int size) {
        return notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    public long contarNoLeidas(String usuarioId) {
        return notificacionRepository.countByUsuarioIdAndLeida(usuarioId, false);
    }

    @Transactional
    public NotificacionDTO marcarLeida(String id) {
        notificacionRepository.marcarComoLeida(id);
        return notificacionRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Transactional
    public boolean marcarTodasLeidas(String usuarioId) {
        notificacionRepository.marcarTodasComoLeidas(usuarioId);
        return true;
    }

    public Flux<NotificacionDTO> suscribirseANotificaciones(String usuarioId) {
        return notificacionSink.asFlux()
                .filter(n -> n.getUsuarioId().equals(usuarioId));
    }

    @Async
    @EventListener
    public void onQuejaEstadoCambiado(QuejaEstadoCambiado event) {
        String cuerpo = "Tu queja ha cambiado de estado: " + event.estadoAnteriorClave()
                + " → " + event.estadoNuevoClave();
        notificarDuennoQueja(event.quejaId(), "QUEJA_ESTADO_CAMBIADO",
                "Estado de queja actualizado", cuerpo);
    }

    @Async
    @EventListener
    public void onQuejaClasificada(QuejaClasificada event) {
        String cuerpo = "Tu queja ha sido clasificada con nivel: " + event.nivelRiesgo();
        notificarDuennoQueja(event.quejaId(), "QUEJA_CLASIFICADA",
                "Queja clasificada", cuerpo);
    }

    @Async
    @EventListener
    public void onMensajeRecibido(MensajeEvent event) {
        crearNotificacion(event.getDestinatarioId(), "NUEVO_MENSAJE",
                "Nuevo mensaje recibido",
                "Tienes un nuevo mensaje",
                event.getMensaje().getConversacionId());
    }

    private void notificarDuennoQueja(String quejaId, String tipo, String titulo, String cuerpo) {
        quejaRepository.buscarPorId(quejaId).ifPresent(queja ->
                crearNotificacion(queja.getUsuarioId(), tipo, titulo, cuerpo, quejaId)
        );
    }

    private void crearNotificacion(String usuarioId, String tipo, String titulo, String cuerpo, String referenciaId) {
        Notificacion notificacion = new Notificacion(usuarioId, tipo, titulo, cuerpo, referenciaId);
        Notificacion saved = notificacionRepository.save(notificacion);
        NotificacionDTO dto = toDTO(saved);
        Sinks.EmitResult result = notificacionSink.tryEmitNext(dto);
        if (result.isFailure()) {
            log.warn("notificacionSink emit result: {} for usuarioId={}, tipo={}", result, usuarioId, tipo);
        }
    }

    private NotificacionDTO toDTO(Notificacion entity) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(entity.getId());
        dto.setUsuarioId(entity.getUsuarioId());
        dto.setTipo(entity.getTipo());
        dto.setTitulo(entity.getTitulo());
        dto.setCuerpo(entity.getCuerpo());
        dto.setReferenciaId(entity.getReferenciaId());
        dto.setLeida(entity.getLeida());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaLectura(entity.getFechaLectura());
        return dto;
    }
}
