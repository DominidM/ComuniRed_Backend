package com.comunired.asignaciones.application.service;

import com.comunired.asignaciones.application.dto.AsignacionesDTO;
import com.comunired.asignaciones.domain.entity.Asignaciones;
import com.comunired.asignaciones.domain.repository.AsignacionesRepository;
import com.comunired.historial_evento.application.service.HistorialEventoService;
import com.comunired.quejas.application.port.out.QuejaOutPorts.QuejaRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsignacionesService {

    private final AsignacionesRepository asignacionesRepository;
    private final QuejaRepositoryPort quejaRepository;   // ← port, no repo viejo
    private final HistorialEventoService historialEventoService;

    public AsignacionesService(AsignacionesRepository asignacionesRepository,
                                QuejaRepositoryPort quejaRepository,
                                HistorialEventoService historialEventoService) {
        this.asignacionesRepository = asignacionesRepository;
        this.quejaRepository = quejaRepository;
        this.historialEventoService = historialEventoService;
    }

    public AsignacionesDTO asignar(String quejaId, String soporteId, String adminId, String observacion) {
        // Validar que la queja existe usando el nuevo port
        if (!quejaRepository.existePorId(quejaId)) {
            throw new RuntimeException("Queja no encontrada: " + quejaId);
        }

        Asignaciones asignacion = new Asignaciones();
        asignacion.setQueja_id(quejaId);
        asignacion.setSoporte_id(soporteId);
        asignacion.setAsignado_por_id(adminId);
        asignacion.setEstado("PENDIENTE");
        asignacion.setObservacion(observacion);

        Asignaciones saved = asignacionesRepository.save(asignacion);

        historialEventoService.registrar(
                quejaId, adminId, "asignada", null, "ASIGNADA",
                "Asignada a soporte ID: " + soporteId + (observacion != null ? " - " + observacion : "")
        );

        return toDTO(saved);
    }

    public AsignacionesDTO cambiarEstado(String asignacionId, String nuevoEstado, String soporteId) {
        Asignaciones a = asignacionesRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        String estadoAnterior = a.getEstado();
        a.setEstado(nuevoEstado);
        a.setFecha_actualizacion(Instant.now());
        Asignaciones updated = asignacionesRepository.save(a);

        historialEventoService.registrar(
                a.getQueja_id(), soporteId, "estado_asignacion_cambiado",
                estadoAnterior, nuevoEstado,
                "Estado de asignación cambiado de " + estadoAnterior + " a " + nuevoEstado
        );

        return toDTO(updated);
    }

    public AsignacionesDTO reasignar(String asignacionId, String nuevoSoporteId, String adminId, String motivo) {
        Asignaciones a = asignacionesRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        String soporteAnterior = a.getSoporte_id();
        a.setSoporte_id(nuevoSoporteId);
        a.setEstado("REASIGNADA");
        a.setFecha_actualizacion(Instant.now());
        a.setObservacion(motivo);
        Asignaciones updated = asignacionesRepository.save(a);

        historialEventoService.registrar(
                a.getQueja_id(), adminId, "reasignada",
                "Soporte: " + soporteAnterior, "Soporte: " + nuevoSoporteId,
                motivo != null ? motivo : "Reasignada a nuevo soporte"
        );

        return toDTO(updated);
    }

    public AsignacionesDTO findById(String id) {
        return toDTO(asignacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada")));
    }

    public List<AsignacionesDTO> findAllByQuejaId(String quejaId) {
        return asignacionesRepository.findByQuejaId(quejaId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<AsignacionesDTO> findBySoporteId(String soporteId) {
        return asignacionesRepository.findBySoporteId(soporteId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<AsignacionesDTO> findActivas() {
        return asignacionesRepository.findByEstado("PENDIENTE").stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    private AsignacionesDTO toDTO(Asignaciones a) {
        AsignacionesDTO dto = new AsignacionesDTO();
        dto.setId(a.getId());
        dto.setQueja_id(a.getQueja_id());
        dto.setSoporte_id(a.getSoporte_id());
        dto.setAsignado_por_id(a.getAsignado_por_id());
        dto.setEstado(a.getEstado());
        dto.setFecha_asignacion(a.getFecha_asignacion());
        dto.setFecha_actualizacion(a.getFecha_actualizacion());
        dto.setObservacion(a.getObservacion());
        return dto;
    }
}
