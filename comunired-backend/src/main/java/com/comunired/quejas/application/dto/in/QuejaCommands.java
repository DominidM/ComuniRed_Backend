package com.comunired.quejas.application.dto.in;

/**
 * Commands de entrada para el módulo quejas.
 * Son objetos inmutables que representan la intención del cliente.
 */
public final class QuejaCommands {

    private QuejaCommands() {}

    // POST /api/quejas — ciudadano crea una queja con imagen
    public record CrearQuejaCommand(
            String titulo,
            String descripcion,
            String categoriaId,
            String ubicacion,
            String usuarioId,
            String imagenUrl          // llega ya subida a Cloudinary desde el controller REST
    ) {}

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
