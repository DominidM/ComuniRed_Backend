package com.comunired.quejas.domain.event;

import java.time.Instant;

public final class QuejaEvents {

    private QuejaEvents() {}

    public record QuejaCreada(
            String quejaId,
            String usuarioId,
            String categoriaId,
            String titulo,
            Instant fechaCreacion
    ) {}

    public record QuejaEstadoCambiado(
            String quejaId,
            String usuarioId,
            String estadoAnteriorClave,
            String estadoNuevoClave,
            String observacion,
            Instant fecha
    ) {}

    public record QuejaClasificada(
            String quejaId,
            String soporteId,
            String nivelRiesgo,
            String observacion,
            Instant fecha
    ) {}

    public record QuejaEliminada(
            String quejaId,
            String usuarioId,
            Instant fecha
    ) {}
}
