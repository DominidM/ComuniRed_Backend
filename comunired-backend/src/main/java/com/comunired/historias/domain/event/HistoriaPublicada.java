package com.comunired.historias.domain.event;

import java.time.Instant;

public record HistoriaPublicada(
    String historiaId,
    String usuarioId,
    String distrito,
    Instant fechaCreacion
) {}