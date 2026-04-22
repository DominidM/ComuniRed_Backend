package com.comunired.reacciones.application.service;

import com.comunired.reacciones.domain.entity.Reacciones;
import com.comunired.reacciones.domain.repository.ReaccionesRepository;
import com.comunired.reacciones.application.dto.ReaccionesDTO;
import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import com.comunired.usuarios.application.dto.UsuariosDTO;
import com.comunired.tipos_reaccion.domain.repository.Tipos_reaccionRepository;
import com.comunired.tipos_reaccion.application.dto.Tipos_reaccionDTO;

import com.comunired.quejas.application.dto.QuejasDTO.ReactionsDTO;
import com.comunired.quejas.application.service.QuejasService;   // ← NUEVO
import com.comunired.quejas.application.dto.QuejasDTO;           // ← NUEVO
import com.comunired.quejas.domain.repository.QuejasRepository;  // ← NUEVO
import com.comunired.quejas.domain.entity.Quejas;                // ← NUEVO
import com.comunired.estados_queja.domain.repository.Estados_quejaRepository; // ← NUEVO
import com.comunired.estados_queja.domain.entity.Estados_queja;               // ← NUEVO

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReaccionesService {

    // Mínimo de votos "accept" para pasar a PENDIENTE
    private static final int VOTOS_MINIMOS = 5;

    @Autowired
    private ReaccionesRepository reaccionesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private Tipos_reaccionRepository tiposReaccionRepository;

    @Autowired
    private QuejasRepository quejasRepository;              // ← NUEVO

    @Autowired
    private Estados_quejaRepository estadosRepository;      // ← NUEVO

    @Autowired
    @Lazy                                                    // ← evita dependencia circular
    private QuejasService quejasService;                    // ← NUEVO

    public ReactionsDTO toggleReaction(String quejaId, String tipoReaccionKey, String usuarioId) {

        Optional<String> tipoReaccionIdOpt = tiposReaccionRepository.buscarPorKey(tipoReaccionKey)
                .map(t -> t.getId());

        if (!tipoReaccionIdOpt.isPresent()) {
            throw new RuntimeException("Tipo de reacción no encontrado: " + tipoReaccionKey);
        }

        String tipoReaccionId = tipoReaccionIdOpt.get();

        Optional<Reacciones> existingReaction = reaccionesRepository
                .findByQuejaIdAndUsuarioId(quejaId, usuarioId);

        if (existingReaction.isPresent()) {
            Reacciones reaccion = existingReaction.get();

            if (reaccion.getTipo_reaccion_id().equals(tipoReaccionId)) {
                reaccionesRepository.delete(reaccion);
            } else {
                reaccion.setTipo_reaccion_id(tipoReaccionId);
                reaccionesRepository.save(reaccion);
            }
        } else {
            Reacciones nuevaReaccion = new Reacciones();
            nuevaReaccion.setQueja_id(quejaId);
            nuevaReaccion.setUsuario_id(usuarioId);
            nuevaReaccion.setTipo_reaccion_id(tipoReaccionId);
            reaccionesRepository.save(nuevaReaccion);
        }

        // ── TRANSICIÓN AUTOMÁTICA: VOTACION → PENDIENTE ──────────────────
        // Solo se evalúa cuando el voto es "accept"
        if ("accept".equalsIgnoreCase(tipoReaccionKey)) {
            verificarYPasarAPendiente(quejaId, usuarioId);
        }
        // ─────────────────────────────────────────────────────────────────

        return getReactionsForQueja(quejaId, usuarioId);
    }

    /**
     * Si la queja está en VOTACION y los votos "accept" alcanzan el mínimo,
     * la pasa automáticamente a PENDIENTE.
     */
    private void verificarYPasarAPendiente(String quejaId, String usuarioId) {
        try {
            // 1. Obtener la queja
            Optional<Quejas> quejaOpt = quejasRepository.findById(quejaId);
            if (!quejaOpt.isPresent()) return;
            Quejas queja = quejaOpt.get();

            // 2. Verificar que esté en estado VOTACION
            String claveEstadoActual = estadosRepository.buscarPorId(queja.getEstado_id())
                    .map(Estados_queja::getClave)
                    .orElse("");

            if (!"VOTACION".equalsIgnoreCase(claveEstadoActual)) return;

            // 3. Contar votos "accept"
            Optional<String> acceptIdOpt = tiposReaccionRepository.buscarPorKey("accept")
                    .map(t -> t.getId());

            if (!acceptIdOpt.isPresent()) return;

            long votosAccept = reaccionesRepository
                    .countByQuejaIdAndTipoReaccionId(quejaId, acceptIdOpt.get());

            System.out.println("📊 Votos accept para queja " + quejaId + ": " + votosAccept + "/" + VOTOS_MINIMOS);

            // 4. Si alcanza el mínimo → pasar a PENDIENTE
            if (votosAccept >= VOTOS_MINIMOS) {
                System.out.println("✅ Mínimo alcanzado → pasando a PENDIENTE");
                quejasService.cambiarEstadoQueja(
                    quejaId,
                    "sistema",
                    "PENDIENTE",
                    "Mínimo de votos alcanzado (" + votosAccept + "/" + VOTOS_MINIMOS + ")"
                );
            }

        } catch (Exception e) {
            // No interrumpir el flujo de votación si algo falla aquí
            System.out.println("⚠️ Error en verificarYPasarAPendiente: " + e.getMessage());
        }
    }

    public List<ReaccionesDTO> findByQuejaId(String quejaId) {
        return reaccionesRepository.findByQuejaId(quejaId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<UsuariosDTO> getUsersByReactionType(String quejaId, String tipoReaccionKey) {
        Optional<String> tipoReaccionIdOpt = tiposReaccionRepository.buscarPorKey(tipoReaccionKey)
                .map(t -> t.getId());

        if (!tipoReaccionIdOpt.isPresent()) {
            return new ArrayList<>();
        }

        String tipoReaccionId = tipoReaccionIdOpt.get();
        List<Reacciones> reacciones = reaccionesRepository.findByQuejaId(quejaId);

        return reacciones.stream()
                .filter(r -> r.getTipo_reaccion_id().equals(tipoReaccionId))
                .map(r -> {
                    Usuario usuario = usuariosRepository.findById(r.getUsuario_id());
                    if (usuario == null) return null;

                    UsuariosDTO dto = new UsuariosDTO();
                    dto.setId(usuario.getId());
                    dto.setNombre(usuario.getNombre());
                    dto.setApellido(usuario.getApellido());
                    dto.setFoto_perfil(usuario.getFoto_perfil());
                    return dto;
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    private ReactionsDTO getReactionsForQueja(String quejaId, String usuarioId) {
        List<String> excludeKeys = Arrays.asList("accept", "reject");
        List<Reacciones> allReactions = reaccionesRepository.findByQuejaId(quejaId);

        Map<String, Long> countsMap = new HashMap<>();
        String userReaction = null;

        for (Reacciones reaccion : allReactions) {
            Optional<com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion> tipoOpt =
                tiposReaccionRepository.buscarPorId(reaccion.getTipo_reaccion_id());

            if (tipoOpt.isPresent()) {
                com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion tipo = tipoOpt.get();
                String key = tipo.getKey();

                if (!excludeKeys.contains(key)) {
                    countsMap.put(key, countsMap.getOrDefault(key, 0L) + 1);

                    if (reaccion.getUsuario_id().equals(usuarioId)) {
                        userReaction = key;
                    }
                }
            }
        }

        ReactionsDTO reactionsDTO = new ReactionsDTO();

        Map<String, Long> counts = new HashMap<>();
        counts.put("like",    countsMap.getOrDefault("like",    0L));
        counts.put("love",    countsMap.getOrDefault("love",    0L));
        counts.put("wow",     countsMap.getOrDefault("wow",     0L));
        counts.put("helpful", countsMap.getOrDefault("helpful", 0L));
        counts.put("dislike", countsMap.getOrDefault("dislike", 0L));
        counts.put("report",  countsMap.getOrDefault("report",  0L));

        reactionsDTO.setCounts(counts);
        reactionsDTO.setUserReaction(userReaction);
        reactionsDTO.setTotal(countsMap.values().stream().mapToLong(Long::longValue).sum());

        return reactionsDTO;
    }

    public long contarReaccionesPorUsuario(String usuarioId) {
        return reaccionesRepository.countByUsuarioId(usuarioId);
    }

    private ReaccionesDTO toDTO(Reacciones reaccion) {
        ReaccionesDTO dto = new ReaccionesDTO();
        dto.setId(reaccion.getId());
        dto.setQueja_id(reaccion.getQueja_id());
        dto.setFecha_reaccion(reaccion.getFecha_reaccion());

        if (reaccion.getUsuario_id() != null) {
            Usuario usuario = usuariosRepository.findById(reaccion.getUsuario_id());
            if (usuario != null) {
                UsuariosDTO usuarioDTO = new UsuariosDTO();
                usuarioDTO.setId(usuario.getId());
                usuarioDTO.setNombre(usuario.getNombre());
                usuarioDTO.setApellido(usuario.getApellido());
                usuarioDTO.setFoto_perfil(usuario.getFoto_perfil());
                dto.setUsuario(usuarioDTO);
            }
        }

        if (reaccion.getTipo_reaccion_id() != null) {
            Optional<com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion> tipoOpt =
                tiposReaccionRepository.buscarPorId(reaccion.getTipo_reaccion_id());

            if (tipoOpt.isPresent()) {
                com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion tipo = tipoOpt.get();
                Tipos_reaccionDTO tipoDTO = new Tipos_reaccionDTO();
                tipoDTO.setId(tipo.getId());
                tipoDTO.setKey(tipo.getKey());
                tipoDTO.setLabel(tipo.getLabel());
                dto.setTipoReaccion(tipoDTO);
            }
        }

        return dto;
    }
}