package com.comunired.seguimientos.application.service;

import com.comunired.seguimientos.application.dto.SeguimientoDTO;
import com.comunired.seguimientos.domain.entity.Seguimiento;
import com.comunired.seguimientos.domain.entity.Seguimiento.EstadoSeguimiento;
import com.comunired.seguimientos.domain.repository.SeguimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class SeguimientoService {
    
    @Autowired
    private SeguimientoRepository seguimientoRepository;

    @Transactional
    public SeguimientoDTO enviarSolicitud(String seguidorId, String seguidoId) {
        if (seguidorId.equals(seguidoId)) {
            throw new IllegalArgumentException("No puedes seguirte a ti mismo");
        }
        
        if (seguimientoRepository.existsBySeguidorIdAndSeguidoId(seguidorId, seguidoId)) {
            throw new IllegalStateException("Ya existe una solicitud para este usuario");
        }

        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setSeguidorId(seguidorId);
        seguimiento.setSeguidoId(seguidoId);
        seguimiento.setEstado(EstadoSeguimiento.PENDIENTE);
        
        Seguimiento saved = seguimientoRepository.save(seguimiento);
        return toDTO(saved);
    }

    @Transactional
    public SeguimientoDTO aceptarSolicitud(String seguimientoId) {
        Seguimiento seguimiento = seguimientoRepository.findById(seguimientoId)
                .orElseThrow(() -> new IllegalArgumentException("Seguimiento no encontrado"));
        
        if (seguimiento.getEstado() != EstadoSeguimiento.PENDIENTE) {
            throw new IllegalStateException("Esta solicitud ya fue respondida");
        }
        
        seguimiento.setEstado(EstadoSeguimiento.ACEPTADO);
        seguimiento.setFechaRespuesta(Instant.now());
        
        Seguimiento saved = seguimientoRepository.save(seguimiento);
        return toDTO(saved);
    }

    @Transactional
    public boolean rechazarSolicitud(String seguimientoId) {
        Seguimiento seguimiento = seguimientoRepository.findById(seguimientoId)
                .orElseThrow(() -> new IllegalArgumentException("Seguimiento no encontrado"));
        
        if (seguimiento.getEstado() != EstadoSeguimiento.PENDIENTE) {
            throw new IllegalStateException("Esta solicitud ya fue respondida");
        }
        
        seguimientoRepository.deleteById(seguimientoId);
        return true;
    }

    @Transactional
    public boolean cancelarSolicitud(String seguimientoId) {
        Seguimiento seguimiento = seguimientoRepository.findById(seguimientoId)
                .orElseThrow(() -> new IllegalArgumentException("Seguimiento no encontrado"));
        
        if (seguimiento.getEstado() != EstadoSeguimiento.PENDIENTE) {
            throw new IllegalStateException("Solo puedes cancelar solicitudes pendientes");
        }
        
        seguimientoRepository.deleteById(seguimientoId);
        return true;
    }

    @Transactional
    public boolean dejarDeSeguir(String seguidorId, String seguidoId) {
        seguimientoRepository.deleteBySeguidorIdAndSeguidoId(seguidorId, seguidoId);
        return true;
    }

    public Page<SeguimientoDTO> obtenerSolicitudesPendientes(String usuarioId, int page, int size) {
        return seguimientoRepository.findBySeguidoIdAndEstado(usuarioId, 
                EstadoSeguimiento.PENDIENTE, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    public Page<SeguimientoDTO> obtenerSolicitudesEnviadas(String usuarioId, int page, int size) {
        return seguimientoRepository.findBySeguidorIdAndEstado(usuarioId, 
                EstadoSeguimiento.PENDIENTE, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    public Page<SeguimientoDTO> obtenerSeguidores(String usuarioId, int page, int size) {
        return seguimientoRepository.findBySeguidoIdAndEstado(usuarioId, 
                EstadoSeguimiento.ACEPTADO, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    public Page<SeguimientoDTO> obtenerSeguidos(String usuarioId, int page, int size) {
        return seguimientoRepository.findBySeguidorIdAndEstado(usuarioId, 
                EstadoSeguimiento.ACEPTADO, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    public boolean estaSiguiendo(String seguidorId, String seguidoId) {
        return seguimientoRepository.existsBySeguidorIdAndSeguidoIdAndEstado(
                seguidorId, seguidoId, EstadoSeguimiento.ACEPTADO);
    }

    public boolean seSiguenMutuamente(String usuario1Id, String usuario2Id) {
        boolean usuario1SigueUsuario2 = seguimientoRepository
                .existsBySeguidorIdAndSeguidoIdAndEstado(usuario1Id, usuario2Id, EstadoSeguimiento.ACEPTADO);
        boolean usuario2SigueUsuario1 = seguimientoRepository
                .existsBySeguidorIdAndSeguidoIdAndEstado(usuario2Id, usuario1Id, EstadoSeguimiento.ACEPTADO);
        
        return usuario1SigueUsuario2 && usuario2SigueUsuario1;
    }

    public long contarSeguidores(String usuarioId) {
        return seguimientoRepository.countBySeguidoIdAndEstado(usuarioId, EstadoSeguimiento.ACEPTADO);
    }

    public long contarSeguidos(String usuarioId) {
        return seguimientoRepository.countBySeguidorIdAndEstado(usuarioId, EstadoSeguimiento.ACEPTADO);
    }

    private SeguimientoDTO toDTO(Seguimiento entity) {
        SeguimientoDTO dto = new SeguimientoDTO();
        dto.setId(entity.getId());
        dto.setSeguidorId(entity.getSeguidorId());
        dto.setSeguidoId(entity.getSeguidoId());
        dto.setEstado(entity.getEstado().toString());
        dto.setFechaSeguimiento(entity.getFechaSeguimiento());
        dto.setFechaRespuesta(entity.getFechaRespuesta());
        dto.setNotificacionesActivas(entity.getNotificacionesActivas());
        return dto;
    }
}
