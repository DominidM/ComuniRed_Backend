package com.comunired.reels.application.dto.out;

import java.time.Instant;

public record ReelComentarioResponse(
    String id,
    String reelId,
    String usuarioId,
    String usuarioNombre,
    String usuarioAvatar,
    String texto,
    Instant fechaCreacion
) {}
