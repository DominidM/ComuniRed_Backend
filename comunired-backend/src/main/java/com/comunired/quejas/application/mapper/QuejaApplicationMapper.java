package com.comunired.quejas.application.mapper;

import com.comunired.quejas.application.dto.out.QuejaResponses.*;
import com.comunired.quejas.application.port.out.QuejaOutPorts.*;
import com.comunired.quejas.domain.entity.Queja;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public QuejaResponse toResponse(Queja queja) {
        return buildResponse(queja, null, false);
    }

    public QuejaResponse toResponseConContexto(Queja queja, String usuarioActualId) {
        return buildResponse(queja, usuarioActualId, true);
    }

    private QuejaResponse buildResponse(Queja queja, String usuarioActualId, boolean cargarSocial) {

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
            String userReaccion = (usuarioActualId != null)
                    ? reaccionPort.obtenerReaccionUsuario(queja.getId(), usuarioActualId).orElse(null)
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

            canVote = (usuarioActualId != null) && !votoPort.yaVoto(queja.getId(), usuarioActualId);
            userVote = (usuarioActualId != null)
                    ? votoPort.obtenerVotoUsuario(queja.getId(), usuarioActualId).orElse(null)
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
                userVote
        );
    }
}