package com.comunired.asignaciones.application.service;

import com.comunired.asignaciones.domain.entity.Asignaciones;
import com.comunired.asignaciones.domain.repository.AsignacionesRepository;
import com.comunired.asignaciones.application.dto.AsignacionesDTO;
import com.comunired.quejas.domain.repository.QuejasRepository;
import com.comunired.quejas.domain.entity.Quejas;
import com.comunired.historial_evento.application.service.HistorialEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsignacionesService {

    @Autowired
    private AsignacionesRepository asignacionesRepository;

    @Autowired
    private QuejasRepository quejasRepository;

    @Autowired
    private HistorialEventoService historialEventoService;

    public AsignacionesDTO asignar(String quejaId, String soporteId, String adminId, String observacion) {
        System.out.println("🔹 INICIO asignar()");
        System.out.println("  - quejaId: " + quejaId);
        System.out.println("  - soporteId: " + soporteId);
        System.out.println("  - adminId: " + adminId);
        
        // Validar que la queja existe
        Optional<Quejas> quejaOpt = quejasRepository.findById(quejaId);
        if (!quejaOpt.isPresent()) {
            System.out.println("❌ Queja NO encontrada");
            throw new RuntimeException("Queja no encontrada");
        }
        System.out.println("✅ Queja encontrada");

        // Crear asignación
        Asignaciones asignacion = new Asignaciones();
        asignacion.setQueja_id(quejaId);
        asignacion.setSoporte_id(soporteId);
        asignacion.setAsignado_por_id(adminId);
        asignacion.setEstado("PENDIENTE");
        asignacion.setObservacion(observacion);

        System.out.println("🔹 Guardando en BD...");
        Asignaciones saved = asignacionesRepository.save(asignacion);
        System.out.println("✅ Guardado con ID: " + saved.getId());

        // Registrar en historial
        historialEventoService.registrar(
            quejaId,
            adminId,
            "asignada",
            null,
            "ASIGNADA",
            "Asignada a soporte ID: " + soporteId + (observacion != null ? " - " + observacion : "")
        );

        AsignacionesDTO dto = toDTO(saved);
        System.out.println("✅ DTO creado: " + dto.getId());
        
        return dto;
    }

    public AsignacionesDTO cambiarEstado(String asignacionId, String nuevoEstado, String soporteId) {
        Asignaciones asignacion = asignacionesRepository.findById(asignacionId)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        
        String estadoAnterior = asignacion.getEstado();
        asignacion.setEstado(nuevoEstado);
        asignacion.setFecha_actualizacion(Instant.now());
        
        Asignaciones updated = asignacionesRepository.save(asignacion);
        
        historialEventoService.registrar(
            asignacion.getQueja_id(),
            soporteId,
            "estado_asignacion_cambiado",
            estadoAnterior,
            nuevoEstado,
            "Estado de asignación cambiado de " + estadoAnterior + " a " + nuevoEstado
        );
        
        return toDTO(updated);
    }

    public AsignacionesDTO reasignar(String asignacionId, String nuevoSoporteId, String adminId, String motivo) {
        Asignaciones asignacion = asignacionesRepository.findById(asignacionId)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        
        String soporteAnterior = asignacion.getSoporte_id();
        asignacion.setSoporte_id(nuevoSoporteId);
        asignacion.setEstado("REASIGNADA");
        asignacion.setFecha_actualizacion(Instant.now());
        asignacion.setObservacion(motivo);
        
        Asignaciones updated = asignacionesRepository.save(asignacion);
        
        historialEventoService.registrar(
            asignacion.getQueja_id(),
            adminId,
            "reasignada",
            "Soporte: " + soporteAnterior,
            "Soporte: " + nuevoSoporteId,
            motivo != null ? motivo : "Reasignada a nuevo soporte"
        );
        
        return toDTO(updated);
    }

    public AsignacionesDTO findById(String id) {
        Asignaciones asignacion = asignacionesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        return toDTO(asignacion);
    }

    public List<AsignacionesDTO> findAllByQuejaId(String quejaId) {
        return asignacionesRepository.findByQuejaId(quejaId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<AsignacionesDTO> findBySoporteId(String soporteId) {
        return asignacionesRepository.findBySoporteId(soporteId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<AsignacionesDTO> findActivas() {
        return asignacionesRepository.findByEstado("PENDIENTE")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AsignacionesDTO toDTO(Asignaciones asignacion) {
        AsignacionesDTO dto = new AsignacionesDTO();
        dto.setId(asignacion.getId());
        dto.setQueja_id(asignacion.getQueja_id());
        dto.setSoporte_id(asignacion.getSoporte_id());
        dto.setAsignado_por_id(asignacion.getAsignado_por_id());
        dto.setEstado(asignacion.getEstado());
        dto.setFecha_asignacion(asignacion.getFecha_asignacion());
        dto.setFecha_actualizacion(asignacion.getFecha_actualizacion());
        dto.setObservacion(asignacion.getObservacion());
        return dto;
    }
}
