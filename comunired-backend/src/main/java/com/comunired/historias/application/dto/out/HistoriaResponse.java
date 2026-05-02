package com.comunired.historias.application.dto.out;

import java.time.Instant;
import java.util.List;

public record HistoriaResponse(
    String id,
    String usuarioId,
    String texto,
    String imagenUrl,
    String colorFondo,
    int duracion,
    boolean activa,
    Instant fechaCreacion,
    Instant fechaExpiracion,
    int totalVistas,
    boolean vistaPorMi
) {}