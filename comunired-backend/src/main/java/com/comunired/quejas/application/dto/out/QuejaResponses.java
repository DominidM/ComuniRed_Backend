package com.comunired.quejas.application.dto.out;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class QuejaResponses {

    private QuejaResponses() {}

    public record QuejaResponse(
            String id,
            String titulo,
            String descripcion,
            UsuarioResumen usuario,
            CategoriaResumen categoria,
            EstadoResumen estado,
            String ubicacion,
            String imagen_url,
            Instant fecha_creacion,
            Instant fecha_actualizacion,
            String nivel_riesgo,
            Instant fecha_clasificacion,
            String clasificado_por_id,
            Instant fecha_aprobacion,
            VotosResumen votes,
            ReaccionesResumen reactions,
            List<ComentarioResumen> comments,
            int commentsCount,
            boolean canVote,
            String userVote
    ) {}

    public record QuejaPageResponse(
            List<QuejaResponse> content,
            long totalElements,
            int totalPages,
            int number,
            int size,
            boolean last
    ) {}

    public record UsuarioResumen(
            String id,
            String nombre,
            String apellido,
            String foto_perfil
    ) {}

    public record CategoriaResumen(
            String id,
            String nombre,
            String descripcion
    ) {}

    public record EstadoResumen(
            String id,
            String clave,
            String nombre
    ) {}

    public record VotosResumen(
            long yes,
            long no,
            long total
    ) {}

    public record ReaccionesResumen(
            Map<String, Long> counts,
            String userReaction,
            long total
    ) {}

    public record ComentarioResumen(
            String id,
            String texto,
            UsuarioResumen autor,
            Instant fecha_creacion
    ) {}
}