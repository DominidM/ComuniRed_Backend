package com.comunired.quejas.application.dto.in;

import java.util.List;

/**
 * Commands de entrada para el módulo quejas.
 * Son objetos inmutables que representan la intención del cliente.
 */
public final class QuejaCommands {

    private QuejaCommands() {}

    // POST /api/quejas — ciudadano crea una queja con imagen(es) y música
    public record CrearQuejaCommand(
            String titulo,
            String descripcion,
            String categoriaId,
            String ubicacion,
            String usuarioId,
            String imagenUrl,
            List<String> imagenesUrl,
            String musicaUrl,
            String musicaTrack,
            String musicaArtista,
            String musicaCover,
            Double lat,
            Double lng
    ) {
        public CrearQuejaCommand(String titulo, String descripcion, String categoriaId,
                                  String ubicacion, String usuarioId, String imagenUrl,
                                  Double lat, Double lng) {
            this(titulo, descripcion, categoriaId, ubicacion, usuarioId, imagenUrl,
                 List.of(), null, null, null, null, lat, lng);
        }
    }

    // PATCH /api/quejas/{id} — editar datos básicos
    public record ActualizarQuejaCommand(
            String id,
            String titulo,
            String descripcion,
            String categoriaId,
            String ubicacion,
            String imagenUrl
    ) {}

    // PATCH /api/quejas/{id}/riesgo — soporte clasifica riesgo
    public record ClasificarRiesgoCommand(
            String quejaId,
            String soporteId,
            String nivelRiesgo,
            String observacion
    ) {}

    // PATCH /api/quejas/{id}/estado — municipio/admin cambia estado
    public record CambiarEstadoCommand(
            String quejaId,
            String usuarioId,
            String nuevoEstadoClave,
            String observacion
    ) {}

    // DELETE /api/quejas/{id}
    public record EliminarQuejaCommand(
            String quejaId,
            String usuarioId
    ) {}
}
