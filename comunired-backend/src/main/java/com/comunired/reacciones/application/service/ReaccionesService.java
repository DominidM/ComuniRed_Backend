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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReaccionesService {

    @Autowired
    private ReaccionesRepository reaccionesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private Tipos_reaccionRepository tiposReaccionRepository;

    /**
     * ✅ TOGGLE REACCIÓN - Retorna ReactionsDTO con estructura correcta
     */
    public ReactionsDTO toggleReaction(String quejaId, String tipoReaccionKey, String usuarioId) {
        // Buscar el tipo de reacción por key
        Optional<String> tipoReaccionIdOpt = tiposReaccionRepository.buscarPorKey(tipoReaccionKey)
                .map(t -> t.getId());
        
        if (!tipoReaccionIdOpt.isPresent()) {
            throw new RuntimeException("Tipo de reacción no encontrado: " + tipoReaccionKey);
        }
        
        String tipoReaccionId = tipoReaccionIdOpt.get();

        // Buscar si ya existe una reacción del usuario en esta queja
        Optional<Reacciones> existingReaction = reaccionesRepository
                .findByQuejaIdAndUsuarioId(quejaId, usuarioId);

        if (existingReaction.isPresent()) {
            Reacciones reaccion = existingReaction.get();
            
            // Si es la misma reacción, la eliminamos (toggle off)
            if (reaccion.getTipo_reaccion_id().equals(tipoReaccionId)) {
                reaccionesRepository.delete(reaccion);
            } else {
                // Si es diferente, la actualizamos
                reaccion.setTipo_reaccion_id(tipoReaccionId);
                reaccionesRepository.save(reaccion);
            }
        } else {
            // No existe, creamos nueva reacción
            Reacciones nuevaReaccion = new Reacciones();
            nuevaReaccion.setQueja_id(quejaId);
            nuevaReaccion.setUsuario_id(usuarioId);
            nuevaReaccion.setTipo_reaccion_id(tipoReaccionId);
            reaccionesRepository.save(nuevaReaccion);
        }

        // ✅ Retornar el estado actualizado de reacciones
        return getReactionsForQueja(quejaId, usuarioId);
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
            // ✅ LÍNEA CORREGIDA SIN TYPOS
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

        // ✅ CREAR ReactionsDTO
        ReactionsDTO reactionsDTO = new ReactionsDTO();
        
        // ✅ ASEGURAR QUE TODOS LOS TIPOS ESTÉN EN EL MAP
        Map<String, Long> counts = new HashMap<>();
        counts.put("like", countsMap.getOrDefault("like", 0L));
        counts.put("love", countsMap.getOrDefault("love", 0L));
        counts.put("wow", countsMap.getOrDefault("wow", 0L));
        counts.put("helpful", countsMap.getOrDefault("helpful", 0L));
        counts.put("dislike", countsMap.getOrDefault("dislike", 0L));
        counts.put("report", countsMap.getOrDefault("report", 0L));
        
        reactionsDTO.setCounts(counts);
        reactionsDTO.setUserReaction(userReaction);
        reactionsDTO.setTotal(countsMap.values().stream().mapToLong(Long::longValue).sum());

        return reactionsDTO;
    }





    private ReaccionesDTO toDTO(Reacciones reaccion) {
        ReaccionesDTO dto = new ReaccionesDTO();
        dto.setId(reaccion.getId());
        dto.setQueja_id(reaccion.getQueja_id());
        dto.setFecha_reaccion(reaccion.getFecha_reaccion());

        // Usuario
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

        // Tipo Reaccion
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
