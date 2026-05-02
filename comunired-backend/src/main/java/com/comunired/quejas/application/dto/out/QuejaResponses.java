package com.comunired.quejas.application.dto.out;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Responses de salida del módulo quejas.
 * Records inmutables — el mapper los construye, nadie los modifica.
 */
public final class QuejaResponses {

    private QuejaResponses() {}

    // -------------------------------------------------------------------------
    // Respuesta principal de una queja (feed + detalle)
    // -------------------------------------------------------------------------
    public record QuejaResponse(
            String id,
            String titulo,
            String descripcion,
            UsuarioResumen usuario,
            CategoriaResumen categoria,
            EstadoResumen estado,
            String ubicacion,
            String imagenUrl,
            Instant fechaCreacion,
            Instant fechaActualizacion,

            // Clasificación
            String nivelRiesgo,
            Instant fechaClasificacion,
            String clasificadoPorId,
            Instant fechaAprobacion,

            // Social
            VotosResumen votos,
            ReaccionesResumen reacciones,
            List<ComentarioResumen> comentarios,
            int comentariosCount,
            boolean canVote,
            String userVote
    ) {}

    // -------------------------------------------------------------------------
    // Respuesta paginada
    // -------------------------------------------------------------------------
    public record QuejaPageResponse(
            List<QuejaResponse> content,
            long totalElements,
            int totalPages,
            int number,
            int size,
            boolean last
    ) {}

    // -------------------------------------------------------------------------
    // Resúmenes anidados (evitan depender de DTOs de otros dominios)
    // -------------------------------------------------------------------------
    public record UsuarioResumen(
            String id,
            String nombre,
            String apellido,
            String fotoPerfil
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
            Instant fechaCreacion
    ) {}
}
