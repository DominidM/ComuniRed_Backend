package com.comunired.comentarios.application.service;

import com.comunired.comentarios.domain.entity.Comentarios;
import com.comunired.comentarios.domain.repository.ComentariosRepository;
import com.comunired.comentarios.application.dto.ComentariosDTO;
import com.comunired.quejas.domain.repository.QuejasRepository;
import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import com.comunired.usuarios.application.dto.UsuariosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentariosService {

    @Autowired
    private ComentariosRepository comentariosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private QuejasRepository quejasRepository;

    public ComentariosDTO create(String quejaId, String usuarioId, String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new RuntimeException("El texto del comentario no puede estar vacío");
        }

        Comentarios comentario = new Comentarios();
        comentario.setQueja_id(quejaId);
        comentario.setUsuario_id(usuarioId);
        comentario.setTexto(texto.trim());

        Comentarios saved = comentariosRepository.save(comentario);
        return toDTO(saved);
    }

    public ComentariosDTO update(String id, String usuarioId, String texto) {
        Comentarios comentario = comentariosRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        if (!comentario.getUsuario_id().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para editar este comentario");
        }
        
        if (texto == null || texto.trim().isEmpty()) {
            throw new RuntimeException("El texto del comentario no puede estar vacío");
        }
        
        comentario.setTexto(texto.trim());
        comentario.setFecha_modificacion(Instant.now());
        
        Comentarios updated = comentariosRepository.save(comentario);
        return toDTO(updated);
    }

    public ComentariosDTO findById(String id) {
        Comentarios comentario = comentariosRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        return toDTO(comentario);
    }

    public List<ComentariosDTO> findByQuejaId(String quejaId) {
        return comentariosRepository.findByQuejaIdActivos(quejaId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ComentariosDTO> findByUsuarioId(String usuarioId) {
        return comentariosRepository.findByUsuarioId(usuarioId)
                .stream()
                .filter(c -> !c.getEliminado())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ComentariosDTO> searchByText(String texto, String usuarioId) {
        return comentariosRepository.findByUsuarioId(usuarioId)
                .stream()
                .filter(c -> !c.getEliminado())
                .filter(c -> c.getTexto().toLowerCase().contains(texto.toLowerCase()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public long countByQuejaId(String quejaId) {
        return comentariosRepository.findByQuejaIdActivos(quejaId).size();
    }

    public boolean delete(String comentarioId, String usuarioId, String razon) {
        return comentariosRepository.findById(comentarioId)
                .map(comentario -> {
                    if (razon != null && !razon.trim().isEmpty()) {
                        comentario.setEliminado(true);
                        comentario.setEliminado_por(usuarioId);
                        comentario.setRazon_eliminacion(razon.trim());
                        comentario.setFecha_eliminacion(Instant.now());
                        comentariosRepository.save(comentario);
                        return true;
                    } else {
                        if (!comentario.getUsuario_id().equals(usuarioId)) {
                            throw new RuntimeException("No tienes permiso para eliminar este comentario");
                        }
                        comentario.setEliminado(true);
                        comentario.setEliminado_por(usuarioId);
                        comentario.setFecha_eliminacion(Instant.now());
                        comentariosRepository.save(comentario);
                        return true;
                    }
                })
                .orElse(false);
    }

    public List<ComentariosDTO> buscarComentariosPorUsuarioId(String usuarioId) {
        return comentariosRepository.findByUsuarioId(usuarioId)
                .stream()
                .filter(c -> !c.getEliminado())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ComentariosDTO> findComentariosEliminados(String quejaId) {
        return comentariosRepository.findByQuejaIdEliminados(quejaId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ComentariosDTO> obtenerTodosLosComentarios() {
        return comentariosRepository.findAll()
            .stream()
            .filter(c -> !c.getEliminado())
            .map(comentario -> {
                ComentariosDTO dto = toDTO(comentario);
                
                // ✅ Agregar información completa de la queja
                if (comentario.getQueja_id() != null) {
                    quejasRepository.findById(comentario.getQueja_id())
                        .ifPresent(queja -> {
                            dto.setQuejaTitulo(queja.getTitulo());
                            dto.setQuejaDescripcion(queja.getDescripcion());
                            dto.setQuejaImagenUrl(queja.getImagen_url());  // ✅ AGREGAR IMAGEN
                        });
                }
                
                return dto;
            })
            .collect(Collectors.toList());
    }

    public ComentariosDTO aprobarComentario(String comentarioId, String soporteId) {
        Comentarios comentario = comentariosRepository.findById(comentarioId)
            .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        // Limpiar el estado de inapropiado
        comentario.setAprobado_por(soporteId);
        comentario.setFecha_aprobacion(Instant.now());
        
        Comentarios updated = comentariosRepository.save(comentario);
        return toDTO(updated);
    }

    public ComentariosDTO rechazarComentario(String comentarioId, String soporteId, String razon) {
        Comentarios comentario = comentariosRepository.findById(comentarioId)
            .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        comentario.setRechazado(true);
        comentario.setRechazado_por(soporteId);
        comentario.setRazon_rechazo(razon);
        comentario.setFecha_rechazo(Instant.now());
        
        Comentarios updated = comentariosRepository.save(comentario);
        return toDTO(updated);
    }

    private ComentariosDTO toDTO(Comentarios comentario) {
        ComentariosDTO dto = new ComentariosDTO();
        dto.setId(comentario.getId());
        dto.setQueja_id(comentario.getQueja_id());
        dto.setTexto(comentario.getTexto());
        dto.setFecha_creacion(comentario.getFecha_creacion());
        dto.setFecha_modificacion(comentario.getFecha_modificacion());
        
        dto.setEliminado(comentario.getEliminado());
        dto.setEliminado_por(comentario.getEliminado_por());
        dto.setRazon_eliminacion(comentario.getRazon_eliminacion());
        dto.setFecha_eliminacion(comentario.getFecha_eliminacion());

        if (comentario.getUsuario_id() != null) {
            Usuario author = usuariosRepository.findById(comentario.getUsuario_id());
            if (author != null) {
                UsuariosDTO authorDTO = new UsuariosDTO();
                authorDTO.setId(author.getId());
                authorDTO.setNombre(author.getNombre());
                authorDTO.setApellido(author.getApellido());
                authorDTO.setFoto_perfil(author.getFoto_perfil());
                dto.setAuthor(authorDTO);
            }
        }

        return dto;
    }

}
