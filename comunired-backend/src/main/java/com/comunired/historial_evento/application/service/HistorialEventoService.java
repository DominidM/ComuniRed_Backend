package com.comunired.historial_evento.application.service;

import com.comunired.historial_evento.domain.entity.HistorialEvento;
import com.comunired.historial_evento.domain.repository.HistorialEventoRepository;
import com.comunired.historial_evento.application.dto.HistorialEventoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialEventoService {

    @Autowired
    private HistorialEventoRepository historialEventoRepository;

    // ✅ ESTE ES EL MÉTODO QUE USA AsignacionesService
    public void registrar(String quejaId, String usuarioId, String tipoEvento, 
                         String estadoAnterior, String estadoNuevo, String descripcion) {
        HistorialEvento evento = new HistorialEvento();
        evento.setQueja_id(quejaId);
        evento.setUsuario_id(usuarioId);
        evento.setTipo_evento(tipoEvento);
        evento.setEstado_anterior(estadoAnterior);
        evento.setEstado_nuevo(estadoNuevo);
        evento.setDescripcion(descripcion);

        historialEventoRepository.save(evento);
    }

    public HistorialEventoDTO findById(String id) {
        HistorialEvento evento = historialEventoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        return toDTO(evento);
    }

    public List<HistorialEventoDTO> findByQuejaId(String quejaId) {
        return historialEventoRepository.findByQuejaId(quejaId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<HistorialEventoDTO> findByQuejaIdAndTipo(String quejaId, String tipoEvento) {
        return historialEventoRepository.findByQuejaIdAndTipoEvento(quejaId, tipoEvento)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<HistorialEventoDTO> findRecentByQuejaId(String quejaId, int limite) {
        List<HistorialEvento> eventos = historialEventoRepository.findByQuejaId(quejaId);
        return eventos.stream()
                .sorted((a, b) -> b.getFecha_evento().compareTo(a.getFecha_evento()))
                .limit(limite)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<HistorialEventoDTO> findByUsuarioId(String usuarioId) {
        return historialEventoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<HistorialEventoDTO> findAll() {
        return historialEventoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public boolean delete(String id) {
        return historialEventoRepository.findById(id)
                .map(evento -> {
                    historialEventoRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    private HistorialEventoDTO toDTO(HistorialEvento evento) {
        HistorialEventoDTO dto = new HistorialEventoDTO();
        dto.setId(evento.getId());
        dto.setQueja_id(evento.getQueja_id());
        dto.setUsuario_id(evento.getUsuario_id());
        dto.setTipo_evento(evento.getTipo_evento());
        dto.setEstado_anterior(evento.getEstado_anterior());
        dto.setEstado_nuevo(evento.getEstado_nuevo());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFecha_evento(evento.getFecha_evento());
        return dto;
    }
}
