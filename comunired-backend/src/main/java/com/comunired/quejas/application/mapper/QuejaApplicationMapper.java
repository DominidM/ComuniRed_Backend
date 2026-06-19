package com.comunired.quejas.application.mapper;

import com.comunired.quejas.application.dto.out.QuejaResponses.*;
import com.comunired.quejas.application.port.out.QuejaOutPorts.*;
import com.comunired.quejas.domain.entity.Queja;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class QuejaApplicationMapper {

    private final UsuarioQuejaPort usuarioPort;
    private final CategoriaQuejaPort categoriaPort;
    private final EstadoQuejaPort estadoPort;
    private final VotoQuejaPort votoPort;
    private final ReaccionQuejaPort reaccionPort;
    private final ComentarioQuejaPort comentarioPort;

    public QuejaApplicationMapper(UsuarioQuejaPort usuarioPort,
            CategoriaQuejaPort categoriaPort,
            EstadoQuejaPort estadoPort,
            VotoQuejaPort votoPort,
            ReaccionQuejaPort reaccionPort,
            ComentarioQuejaPort comentarioPort) {
        this.usuarioPort = usuarioPort;
        this.categoriaPort = categoriaPort;
        this.estadoPort = estadoPort;
        this.votoPort = votoPort;
        this.reaccionPort = reaccionPort;
        this.comentarioPort = comentarioPort;
    }

    /**
     * Versión ligera para listados de admin. Solo carga usuario y estado — sin
     * votos, reacciones ni comentarios.
     */
    public QuejaResponse toResponseAdmin(Queja queja) {
        UsuarioResumen usuario = usuarioPort.buscarPorId(queja.getUsuarioId())
                .map(u -> new UsuarioResumen(u.id(), u.nombre(), u.apellido(), u.fotoPerfil()))
                .orElse(null);

        EstadoResumen estado = estadoPort.buscarPorId(queja.getEstadoId())
                .map(e -> new EstadoResumen(e.id(), e.clave(), e.nombre()))
                .orElse(null);

        VotosResumen votes = new VotosResumen(0, 0, 0);
        ReaccionesResumen reactions = new ReaccionesResumen(java.util.Map.of(), null, 0);

        return new QuejaResponse(
                queja.getId(),
                queja.getTitulo(),
                queja.getDescripcion(),
                usuario,
                null,
                estado,
                queja.getUbicacion(),
                queja.getImagenUrl(),
                queja.getImagenesUrl(),
                queja.getMusicaUrl(),
                queja.getMusicaTrack(),
                queja.getMusicaArtista(),
                queja.getMusicaCover(),
                queja.getFechaCreacion(),
                queja.getFechaActualizacion(),
                queja.getNivelRiesgo(),
                queja.getFechaClasificacion(),
                queja.getClasificadoPorId(),
                queja.getFechaAprobacion(),
                votes,
                reactions,
                List.of(),
                0,
                false,
                null,
                queja.getLat(),
                queja.getLng()
        );
    }

    public QuejaResponse toResponse(Queja queja) {
        return buildResponse(queja, null, false);
    }

    public QuejaResponse toResponseConContexto(Queja queja, String usuarioActualId) {
        String uid = (usuarioActualId != null && usuarioActualId.isBlank()) ? null : usuarioActualId;
        return buildResponse(queja, uid, true);
    }

    public List<QuejaResponse> toResponseBatchConContexto(List<Queja> quejas, String usuarioActualId) {
        if (quejas.isEmpty()) return List.of();
        String uid = (usuarioActualId != null && usuarioActualId.isBlank()) ? null : usuarioActualId;

        List<String> quejaIds = quejas.stream().map(Queja::getId).distinct().toList();
        List<String> userIds = quejas.stream().map(Queja::getUsuarioId).distinct().toList();
        List<String> catIds = quejas.stream().map(Queja::getCategoriaId).distinct().toList();
        List<String> estadoIds = quejas.stream().map(Queja::getEstadoId).distinct().toList();

        var usuarios = usuarioPort.buscarPorIds(userIds);
        var categorias = categoriaPort.buscarPorIds(catIds);
        var estados = estadoPort.buscarPorIds(estadoIds);

        Map<String, VotoQuejaPort.VotoCounts> votos =
                uid != null ? votoPort.contarVotosPorQuejaIds(quejaIds) : Map.of();
        Map<String, Map<String, Long>> reacciones =
                reaccionPort.contarReaccionesPorQuejaIds(quejaIds);
        Map<String, List<ComentarioQuejaPort.ComentarioInfo>> comentariosMap =
                comentarioPort.buscarPorQuejaIds(quejaIds);

        Map<String, String> votoUsuario =
                uid != null
                        ? votoPort.obtenerVotosUsuarioPorQuejaIds(quejaIds, uid)
                        : Map.of();
        Map<String, String> reaccionUsuario =
                uid != null
                        ? reaccionPort.obtenerReaccionesUsuarioPorQuejaIds(quejaIds, uid)
                        : Map.of();

        return quejas.stream()
                .map(q -> {
                    String qid = q.getId();
                    UsuarioResumen usuario = Optional.ofNullable(usuarios.get(q.getUsuarioId()))
                            .map(u -> new UsuarioResumen(u.id(), u.nombre(), u.apellido(), u.fotoPerfil()))
                            .orElse(null);
                    CategoriaResumen categoria = Optional.ofNullable(categorias.get(q.getCategoriaId()))
                            .map(c -> new CategoriaResumen(c.id(), c.nombre(), c.descripcion()))
                            .orElse(null);
                    EstadoResumen estado = Optional.ofNullable(estados.get(q.getEstadoId()))
                            .map(e -> new EstadoResumen(e.id(), e.clave(), e.nombre()))
                            .orElse(null);

                    var v = votos.getOrDefault(qid, new VotoQuejaPort.VotoCounts(0, 0));
                    VotosResumen votes = new VotosResumen(v.si(), v.no(), v.si() + v.no());

                    var counts = reacciones.getOrDefault(qid, Map.of());
                    long totalReacciones = counts.values().stream().mapToLong(Long::longValue).sum();
                    String userReacc = reaccionUsuario.get(qid);
                    ReaccionesResumen reactions = new ReaccionesResumen(counts, userReacc, totalReacciones);

                    List<ComentarioResumen> comments = comentariosMap.getOrDefault(qid, List.of()).stream()
                            .map(c -> new ComentarioResumen(
                                    c.id(), c.texto(),
                                    new UsuarioResumen(c.usuarioId(), c.usuarioNombre(), c.usuarioApellido(), c.usuarioFoto()),
                                    c.fechaCreacion()))
                            .toList();

                    boolean canVote = usuarioActualId != null && !votoUsuario.containsKey(qid);
                    String userVote = votoUsuario.get(qid);

                    return new QuejaResponse(
                            q.getId(), q.getTitulo(), q.getDescripcion(),
                            usuario, categoria, estado,
                            q.getUbicacion(), q.getImagenUrl(),
                            q.getImagenesUrl(),
                            q.getMusicaUrl(), q.getMusicaTrack(), q.getMusicaArtista(), q.getMusicaCover(),
                            q.getFechaCreacion(), q.getFechaActualizacion(),
                            q.getNivelRiesgo(), q.getFechaClasificacion(),
                            q.getClasificadoPorId(), q.getFechaAprobacion(),
                            votes, reactions, comments, comments.size(), canVote, userVote,
                            q.getLat(), q.getLng()
                    );
                })
                .toList();
    }

    public List<QuejaResponse> toResponseBatch(List<Queja> quejas, String usuarioActualId) {
        if (quejas.isEmpty()) return List.of();

        List<String> userIds = quejas.stream().map(Queja::getUsuarioId).distinct().toList();
        List<String> catIds = quejas.stream().map(Queja::getCategoriaId).distinct().toList();
        List<String> estadoIds = quejas.stream().map(Queja::getEstadoId).distinct().toList();

        var usuarios = usuarioPort.buscarPorIds(userIds);
        var categorias = categoriaPort.buscarPorIds(catIds);
        var estados = estadoPort.buscarPorIds(estadoIds);

        return quejas.stream()
                .map(q -> {
                    UsuarioResumen usuario = Optional.ofNullable(usuarios.get(q.getUsuarioId()))
                            .map(u -> new UsuarioResumen(u.id(), u.nombre(), u.apellido(), u.fotoPerfil()))
                            .orElse(null);
                    CategoriaResumen categoria = Optional.ofNullable(categorias.get(q.getCategoriaId()))
                            .map(c -> new CategoriaResumen(c.id(), c.nombre(), c.descripcion()))
                            .orElse(null);
                    EstadoResumen estado = Optional.ofNullable(estados.get(q.getEstadoId()))
                            .map(e -> new EstadoResumen(e.id(), e.clave(), e.nombre()))
                            .orElse(null);

                    return new QuejaResponse(
                            q.getId(), q.getTitulo(), q.getDescripcion(),
                            usuario, categoria, estado,
                            q.getUbicacion(), q.getImagenUrl(),
                            q.getImagenesUrl(),
                            q.getMusicaUrl(), q.getMusicaTrack(), q.getMusicaArtista(), q.getMusicaCover(),
                            q.getFechaCreacion(), q.getFechaActualizacion(),
                            q.getNivelRiesgo(), q.getFechaClasificacion(),
                            q.getClasificadoPorId(), q.getFechaAprobacion(),
                            new VotosResumen(0, 0, 0),
                            new ReaccionesResumen(java.util.Map.of(), null, 0),
                            List.of(), 0, false, null,
                            q.getLat(), q.getLng()
                    );
                })
                .toList();
    }

    private QuejaResponse buildResponse(Queja queja, String usuarioActualId, boolean cargarSocial) {
        String uid = (usuarioActualId != null && usuarioActualId.isBlank()) ? null : usuarioActualId;

        UsuarioResumen usuario = usuarioPort.buscarPorId(queja.getUsuarioId())
                .map(u -> new UsuarioResumen(u.id(), u.nombre(), u.apellido(), u.fotoPerfil()))
                .orElse(null);

        CategoriaResumen categoria = categoriaPort.buscarPorId(queja.getCategoriaId())
                .map(c -> new CategoriaResumen(c.id(), c.nombre(), c.descripcion()))
                .orElse(null);

        EstadoResumen estado = estadoPort.buscarPorId(queja.getEstadoId())
                .map(e -> new EstadoResumen(e.id(), e.clave(), e.nombre()))
                .orElse(null);

        VotosResumen votes = new VotosResumen(0, 0, 0);
        ReaccionesResumen reactions = new ReaccionesResumen(java.util.Map.of(), null, 0);
        List<ComentarioResumen> comments = List.of();
        boolean canVote = false;
        String userVote = null;

        if (cargarSocial) {
            long yes = votoPort.contarVotosSi(queja.getId());
            long no = votoPort.contarVotosNo(queja.getId());
            votes = new VotosResumen(yes, no, yes + no);

            var counts = reaccionPort.contarReacciones(queja.getId());
            long totalReacciones = counts.values().stream().mapToLong(Long::longValue).sum();
            String userReaccion = (uid != null)
                    ? reaccionPort.obtenerReaccionUsuario(queja.getId(), uid).orElse(null)
                    : null;
            reactions = new ReaccionesResumen(counts, userReaccion, totalReacciones);

            comments = comentarioPort.buscarPorQueja(queja.getId()).stream()
                    .map(c -> new ComentarioResumen(
                    c.id(),
                    c.texto(),
                    new UsuarioResumen(c.usuarioId(), c.usuarioNombre(), c.usuarioApellido(), c.usuarioFoto()),
                    c.fechaCreacion()
            ))
                    .collect(Collectors.toList());

            canVote = (uid != null) && !votoPort.yaVoto(queja.getId(), uid);
            userVote = (uid != null)
                    ? votoPort.obtenerVotoUsuario(queja.getId(), uid).orElse(null)
                    : null;
        }

        return new QuejaResponse(
                queja.getId(),
                queja.getTitulo(),
                queja.getDescripcion(),
                usuario,
                categoria,
                estado,
                queja.getUbicacion(),
                queja.getImagenUrl(),
                queja.getImagenesUrl(),
                queja.getMusicaUrl(),
                queja.getMusicaTrack(),
                queja.getMusicaArtista(),
                queja.getMusicaCover(),
                queja.getFechaCreacion(),
                queja.getFechaActualizacion(),
                queja.getNivelRiesgo(),
                queja.getFechaClasificacion(),
                queja.getClasificadoPorId(),
                queja.getFechaAprobacion(),
                votes,
                reactions,
                comments,
                comments.size(),
                canVote,
                userVote,
                queja.getLat(),
                queja.getLng()
        );
    }
}
