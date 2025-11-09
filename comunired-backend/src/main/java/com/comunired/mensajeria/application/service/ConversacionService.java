package com.comunired.mensajeria.application.service;

import com.comunired.mensajeria.application.dto.ConversacionDTO;
import com.comunired.mensajeria.domain.entity.Conversacion;
import com.comunired.mensajeria.domain.repository.ConversacionRepository;
import com.comunired.seguimientos.application.service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ConversacionService {
    
    @Autowired
    private ConversacionRepository conversacionRepository;
    
    @Autowired
    private SeguimientoService seguimientoService;
    
    @Autowired
    private MensajeService mensajeService;

    @Transactional
    public ConversacionDTO crearConversacion(String usuarioId, String otroUsuarioId) {
        // Validar que se siguen mutuamente
        if (!seguimientoService.seSiguenMutuamente(usuarioId, otroUsuarioId)) {
            throw new IllegalStateException("Solo puedes chatear con usuarios que te siguen mutuamente");
        }
        
        // Verificar si ya existe conversación
        Optional<Conversacion> existente = conversacionRepository
                .findByParticipantes(usuarioId, otroUsuarioId);
        
        if (existente.isPresent()) {
            return toDTO(existente.get());
        }
        
        // Crear nueva conversación
        Conversacion conversacion = new Conversacion();
        conversacion.setParticipante1Id(usuarioId);
        conversacion.setParticipante2Id(otroUsuarioId);
        
        Conversacion saved = conversacionRepository.save(conversacion);
        return toDTO(saved);
    }

    public ConversacionDTO obtenerConversacion(String conversacionId) {
        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));
        return toDTO(conversacion);
    }

    public Optional<ConversacionDTO> buscarConversacionConUsuario(String usuarioId, String otroUsuarioId) {
        return conversacionRepository.findByParticipantes(usuarioId, otroUsuarioId)
                .map(this::toDTO);
    }

    public Page<ConversacionDTO> obtenerMisConversaciones(String usuarioId, int page, int size) {
        return conversacionRepository.findByUsuarioId(usuarioId, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    @Transactional
    public boolean eliminarConversacion(String conversacionId, String usuarioId) {
        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));
        
        if (!conversacion.esParticipante(usuarioId)) {
            throw new IllegalStateException("No tienes permiso para eliminar esta conversación");
        }
        
        conversacionRepository.deleteById(conversacionId);
        return true;
    }

    private ConversacionDTO toDTO(Conversacion entity) {
        ConversacionDTO dto = new ConversacionDTO();
        dto.setId(entity.getId());
        dto.setParticipante1Id(entity.getParticipante1Id());
        dto.setParticipante2Id(entity.getParticipante2Id());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaUltimaActividad(entity.getFechaUltimaActividad());
        
        // Obtener último mensaje si existe
        if (entity.getUltimoMensajeId() != null) {
            mensajeService.obtenerMensaje(entity.getUltimoMensajeId())
                    .ifPresent(dto::setUltimoMensaje);
        }
        
        return dto;
    }
}
