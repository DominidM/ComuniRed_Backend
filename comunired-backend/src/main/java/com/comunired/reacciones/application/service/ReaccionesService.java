package com.comunired.reacciones.application.service;

import com.comunired.reacciones.application.dto.ReaccionesDTO;
import com.comunired.reacciones.application.port.out.QuejaParaReaccionPort;
import com.comunired.reacciones.domain.entity.Reacciones;
import com.comunired.reacciones.domain.repository.ReaccionesRepository;
import com.comunired.tipos_reaccion.application.dto.Tipos_reaccionDTO;
import com.comunired.tipos_reaccion.domain.repository.Tipos_reaccionRepository;
import com.comunired.usuarios.application.dto.UsuariosDTO;
import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReaccionesService {

    private static final int VOTOS_MINIMOS = 5;

    private final ReaccionesRepository reaccionesRepository;
    private final UsuariosRepository usuariosRepository;
    private final Tipos_reaccionRepository tiposReaccionRepository;
    private final QuejaParaReaccionPort quejaPort;           // ← port, no repo directo
    private final ApplicationEventPublisher eventPublisher;  // ← evento, no QuejasService directo

    public ReaccionesService(ReaccionesRepository reaccionesRepository,
                              UsuariosRepository usuariosRepository,
                              Tipos_reaccionRepository tiposReaccionRepository,
                              QuejaParaReaccionPort quejaPort,
                              ApplicationEventPublisher eventPublisher) {
        this.reaccionesRepository = reaccionesRepository;
        this.usuariosRepository = usuariosRepository;
        this.tiposReaccionRepository = tiposReaccionRepository;
        this.quejaPort = quejaPort;
        this.eventPublisher = eventPublisher;
    }

    public ReactionsDTO toggleReaction(String quejaId, String tipoReaccionKey, String usuarioId) {

        String tipoReaccionId = tiposReaccionRepository.buscarPorKey(tipoReaccionKey)
                .map(t -> t.getId())
                .orElseThrow(() -> new RuntimeException("Tipo de reacción no encontrado: " + tipoReaccionKey));

        Optional<Reacciones> existing = reaccionesRepository.findByQuejaIdAndUsuarioId(quejaId, usuarioId);

        if (existing.isPresent()) {
            Reacciones reaccion = existing.get();
            if (reaccion.getTipo_reaccion_id().equals(tipoReaccionId)) {
                reaccionesRepository.delete(reaccion);
            } else {
                reaccion.setTipo_reaccion_id(tipoReaccionId);
                reaccionesRepository.save(reaccion);
            }
        } else {
            Reacciones nueva = new Reacciones();
            nueva.setQueja_id(quejaId);
            nueva.setUsuario_id(usuarioId);
            nueva.setTipo_reaccion_id(tipoReaccionId);
            reaccionesRepository.save(nueva);
        }

        // Transición automática VOTACION → PENDIENTE al votar "accept"
        if ("accept".equalsIgnoreCase(tipoReaccionKey)) {
            verificarYEmitirTransicion(quejaId);
        }

        return buildReactionsDTO(quejaId, usuarioId);
    }

    /**
     * Si la queja está en VOTACION y alcanza los votos mínimos,
     * emite un evento — no llama a QuejasService directamente.
     * El listener en quejas recibe el evento y cambia el estado.
     */
    private void verificarYEmitirTransicion(String quejaId) {
        try {
            String estadoClave = quejaPort.obtenerEstadoClave(quejaId).orElse("");
            if (!"VOTACION".equalsIgnoreCase(estadoClave)) return;

            Optional<String> acceptIdOpt = tiposReaccionRepository.buscarPorKey("accept")
                    .map(t -> t.getId());
            if (acceptIdOpt.isEmpty()) return;

            long votosAccept = reaccionesRepository
                    .countByQuejaIdAndTipoReaccionId(quejaId, acceptIdOpt.get());

            System.out.println("📊 Votos accept para queja " + quejaId + ": " + votosAccept + "/" + VOTOS_MINIMOS);

            if (votosAccept >= VOTOS_MINIMOS) {
                System.out.println("✅ Mínimo alcanzado → emitiendo VotosMinimosAlcanzados");
                // Evento en lugar de llamada directa — elimina la dependencia circular
                eventPublisher.publishEvent(new VotosMinimosAlcanzadosEvent(quejaId, votosAccept));
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error en verificarYEmitirTransicion: " + e.getMessage());
        }
    }

    public List<ReaccionesDTO> findByQuejaId(String quejaId) {
        return reaccionesRepository.findByQuejaId(quejaId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<UsuariosDTO> getUsersByReactionType(String quejaId, String tipoReaccionKey) {
        String tipoReaccionId = tiposReaccionRepository.buscarPorKey(tipoReaccionKey)
                .map(t -> t.getId()).orElse(null);
        if (tipoReaccionId == null) return List.of();

        return reaccionesRepository.findByQuejaId(quejaId).stream()
                .filter(r -> r.getTipo_reaccion_id().equals(tipoReaccionId))
                .map(r -> {
                    Usuario u = usuariosRepository.findById(r.getUsuario_id());
                    if (u == null) return null;
                    UsuariosDTO dto = new UsuariosDTO();
                    dto.setId(u.getId()); dto.setNombre(u.getNombre());
                    dto.setApellido(u.getApellido()); dto.setFoto_perfil(u.getFoto_perfil());
                    return dto;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public long contarReaccionesPorUsuario(String usuarioId) {
        return reaccionesRepository.countByUsuarioId(usuarioId);
    }

    private ReactionsDTO buildReactionsDTO(String quejaId, String usuarioId) {
        List<String> excludeKeys = List.of("accept", "reject");
        List<Reacciones> all = reaccionesRepository.findByQuejaId(quejaId);

        Map<String, Long> countsMap = new HashMap<>();
        String userReaction = null;

        for (Reacciones r : all) {
            tiposReaccionRepository.buscarPorId(r.getTipo_reaccion_id()).ifPresent(tipo -> {
                String key = tipo.getKey();
                if (!excludeKeys.contains(key)) {
                    countsMap.merge(key, 1L, Long::sum);
                    if (r.getUsuario_id().equals(usuarioId)) {
                        // userReaction se asigna abajo — necesitamos variable efectivamente final
                    }
                }
            });
        }

        // userReaction por separado (necesita ser efectivamente final en lambda)
        for (Reacciones r : all) {
            if (r.getUsuario_id().equals(usuarioId)) {
                var tipoOpt = tiposReaccionRepository.buscarPorId(r.getTipo_reaccion_id());
                if (tipoOpt.isPresent() && !excludeKeys.contains(tipoOpt.get().getKey())) {
                    userReaction = tipoOpt.get().getKey();
                }
            }
        }

        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("like",    countsMap.getOrDefault("like",    0L));
        counts.put("love",    countsMap.getOrDefault("love",    0L));
        counts.put("wow",     countsMap.getOrDefault("wow",     0L));
        counts.put("helpful", countsMap.getOrDefault("helpful", 0L));
        counts.put("dislike", countsMap.getOrDefault("dislike", 0L));
        counts.put("report",  countsMap.getOrDefault("report",  0L));

        ReactionsDTO dto = new ReactionsDTO();
        dto.setCounts(counts);
        dto.setUserReaction(userReaction);
        dto.setTotal(counts.values().stream().mapToLong(Long::longValue).sum());
        return dto;
    }

    private ReaccionesDTO toDTO(Reacciones reaccion) {
        ReaccionesDTO dto = new ReaccionesDTO();
        dto.setId(reaccion.getId());
        dto.setQueja_id(reaccion.getQueja_id());
        dto.setFecha_reaccion(reaccion.getFecha_reaccion());

        if (reaccion.getUsuario_id() != null) {
            Usuario u = usuariosRepository.findById(reaccion.getUsuario_id());
            if (u != null) {
                UsuariosDTO uDto = new UsuariosDTO();
                uDto.setId(u.getId()); uDto.setNombre(u.getNombre());
                uDto.setApellido(u.getApellido()); uDto.setFoto_perfil(u.getFoto_perfil());
                dto.setUsuario(uDto);
            }
        }

        tiposReaccionRepository.buscarPorId(reaccion.getTipo_reaccion_id()).ifPresent(tipo -> {
            Tipos_reaccionDTO tDto = new Tipos_reaccionDTO();
            tDto.setId(tipo.getId()); tDto.setKey(tipo.getKey()); tDto.setLabel(tipo.getLabel());
            dto.setTipoReaccion(tDto);
        });

        return dto;
    }

    // -------------------------------------------------------------------------
    // DTO de salida (antes era inner class de QuejasDTO)
    // -------------------------------------------------------------------------
    public static class ReactionsDTO {
        private Map<String, Long> counts;
        private String userReaction;
        private Long total;

        public Map<String, Long> getCounts() { return counts; }
        public void setCounts(Map<String, Long> counts) { this.counts = counts; }
        public String getUserReaction() { return userReaction; }
        public void setUserReaction(String userReaction) { this.userReaction = userReaction; }
        public Long getTotal() { return total; }
        public void setTotal(Long total) { this.total = total; }
    }

    // -------------------------------------------------------------------------
    // Evento interno — quejas lo escucha y cambia el estado
    // -------------------------------------------------------------------------
    public record VotosMinimosAlcanzadosEvent(String quejaId, long votosAccept) {}
}
