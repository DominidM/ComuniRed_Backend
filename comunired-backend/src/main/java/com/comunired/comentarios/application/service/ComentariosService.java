package com.comunired.comentarios.application.service;

import com.comunired.comentarios.application.dto.ComentariosDTO;
import com.comunired.comentarios.domain.entity.Comentarios;
import com.comunired.comentarios.domain.repository.ComentariosRepository;
import com.comunired.quejas.application.port.out.QuejaOutPorts.QuejaRepositoryPort;
import com.comunired.usuarios.application.dto.UsuariosDTO;
import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentariosService {

    private final ComentariosRepository comentariosRepository;
    private final UsuariosRepository usuariosRepository;
    private final QuejaRepositoryPort quejaRepository; // ← port, no repo viejo

    public ComentariosService(ComentariosRepository comentariosRepository,
                               UsuariosRepository usuariosRepository,
                               QuejaRepositoryPort quejaRepository) {
        this.comentariosRepository = comentariosRepository;
        this.usuariosRepository = usuariosRepository;
        this.quejaRepository = quejaRepository;
    }

    public ComentariosDTO create(String quejaId, String usuarioId, String texto) {
        if (texto == null || texto.trim().isEmpty())
            throw new RuntimeException("El texto del comentario no puede estar vacío");

        Comentarios comentario = new Comentarios();
        comentario.setQueja_id(quejaId);
        comentario.setUsuario_id(usuarioId);
        comentario.setTexto(texto.trim());

        return toDTO(comentariosRepository.save(comentario));
    }

    public ComentariosDTO update(String id, String usuarioId, String texto) {
        Comentarios comentario = comentariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        if (!comentario.getUsuario_id().equals(usuarioId))
            throw new RuntimeException("No tienes permiso para editar este comentario");
        if (texto == null || texto.trim().isEmpty())
            throw new RuntimeException("El texto del comentario no puede estar vacío");

        comentario.setTexto(texto.trim());
        comentario.setFecha_modificacion(Instant.now());
        return toDTO(comentariosRepository.save(comentario));
    }

    public ComentariosDTO findById(String id) {
        return toDTO(comentariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado")));
    }

    public List<ComentariosDTO> findByQuejaId(String quejaId) {
        return comentariosRepository.findByQuejaIdActivos(quejaId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ComentariosDTO> findByUsuarioId(String usuarioId) {
        return comentariosRepository.findByUsuarioId(usuarioId)
                .stream().filter(c -> !c.getEliminado()).map(this::toDTO).collect(Collectors.toList());
    }

    public List<ComentariosDTO> searchByText(String texto, String usuarioId) {
        return comentariosRepository.findByUsuarioId(usuarioId).stream()
                .filter(c -> !c.getEliminado())
                .filter(c -> c.getTexto().toLowerCase().contains(texto.toLowerCase()))
                .map(this::toDTO).collect(Collectors.toList());
    }

    public long countByQuejaId(String quejaId) {
        return comentariosRepository.findByQuejaIdActivos(quejaId).size();
    }

    public boolean delete(String comentarioId, String usuarioId, String razon) {
        return comentariosRepository.findById(comentarioId).map(c -> {
            if (razon != null && !razon.trim().isEmpty()) {
                c.setEliminado(true);
                c.setEliminado_por(usuarioId);
                c.setRazon_eliminacion(razon.trim());
                c.setFecha_eliminacion(Instant.now());
            } else {
                if (!c.getUsuario_id().equals(usuarioId))
                    throw new RuntimeException("No tienes permiso para eliminar este comentario");
                c.setEliminado(true);
                c.setEliminado_por(usuarioId);
                c.setFecha_eliminacion(Instant.now());
            }
            comentariosRepository.save(c);
            return true;
        }).orElse(false);
    }

    public List<ComentariosDTO> buscarComentariosPorUsuarioId(String usuarioId) {
        return findByUsuarioId(usuarioId);
    }

    public List<ComentariosDTO> findComentariosEliminados(String quejaId) {
        return comentariosRepository.findByQuejaIdEliminados(quejaId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ComentariosDTO> obtenerTodosLosComentarios() {
        return comentariosRepository.findAll().stream()
                .filter(c -> !c.getEliminado())
                .map(c -> {
                    ComentariosDTO dto = toDTO(c);
                    // Enriquecer con datos de la queja usando el nuevo port
                    if (c.getQueja_id() != null) {
                        quejaRepository.buscarPorId(c.getQueja_id()).ifPresent(q -> {
                            dto.setQuejaTitulo(q.getTitulo());
                            dto.setQuejaDescripcion(q.getDescripcion());
                            dto.setQuejaImagenUrl(q.getImagenUrl());
                        });
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ComentariosDTO aprobarComentario(String comentarioId, String soporteId) {
        Comentarios c = comentariosRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        c.setAprobado_por(soporteId);
        c.setFecha_aprobacion(Instant.now());
        return toDTO(comentariosRepository.save(c));
    }

    public ComentariosDTO rechazarComentario(String comentarioId, String soporteId, String razon) {
        Comentarios c = comentariosRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        c.setRechazado(true);
        c.setRechazado_por(soporteId);
        c.setRazon_rechazo(razon);
        c.setFecha_rechazo(Instant.now());
        return toDTO(comentariosRepository.save(c));
    }

    private ComentariosDTO toDTO(Comentarios c) {
        ComentariosDTO dto = new ComentariosDTO();
        dto.setId(c.getId());
        dto.setQueja_id(c.getQueja_id());
        dto.setTexto(c.getTexto());
        dto.setFecha_creacion(c.getFecha_creacion());
        dto.setFecha_modificacion(c.getFecha_modificacion());
        dto.setEliminado(c.getEliminado());
        dto.setEliminado_por(c.getEliminado_por());
        dto.setRazon_eliminacion(c.getRazon_eliminacion());
        dto.setFecha_eliminacion(c.getFecha_eliminacion());

        if (c.getUsuario_id() != null) {
            Usuario u = usuariosRepository.findById(c.getUsuario_id());
            if (u != null) {
                UsuariosDTO uDto = new UsuariosDTO();
                uDto.setId(u.getId()); uDto.setNombre(u.getNombre());
                uDto.setApellido(u.getApellido()); uDto.setFoto_perfil(u.getFoto_perfil());
                dto.setAuthor(uDto);
            }
        }
        return dto;
    }
}
