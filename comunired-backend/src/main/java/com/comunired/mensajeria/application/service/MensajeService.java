package com.comunired.mensajeria.application.service;

import com.comunired.mensajeria.application.dto.MensajeDTO;
import com.comunired.mensajeria.domain.entity.Conversacion;
import com.comunired.mensajeria.domain.entity.Mensaje;
import com.comunired.mensajeria.domain.repository.ConversacionRepository;
import com.comunired.mensajeria.domain.repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class MensajeService {
    
    @Autowired
    private MensajeRepository mensajeRepository;
    
    @Autowired
    private ConversacionRepository conversacionRepository;

    @Transactional
    public MensajeDTO enviarMensaje(String conversacionId, String emisorId, String contenido) {
        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));
        
        if (!conversacion.esParticipante(emisorId)) {
            throw new IllegalStateException("No eres parte de esta conversación");
        }
        
        // Crear mensaje
        Mensaje mensaje = new Mensaje(conversacionId, emisorId, contenido);
        Mensaje saved = mensajeRepository.save(mensaje);
        
        // Actualizar conversación
        conversacion.setUltimoMensajeId(saved.getId());
        conversacion.setFechaUltimaActividad(Instant.now());
        conversacionRepository.save(conversacion);
        
        return toDTO(saved);
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
        return true;
    }

    public long contarNoLeidos(String conversacionId, String usuarioId) {
        return mensajeRepository.countNoLeidosByConversacionAndReceptor(conversacionId, usuarioId);
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
