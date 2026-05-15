package com.comunired.reels.application.dto.out;

import java.time.Instant;

public record ReelResponse(
    String id,
    String videoUrl,
    String title,
    String description,
    String authorId,
    String author,
    String avatarUrl,
    int likes,
    int shares,
    int vistas,
    int comentariosCount,
    boolean liked,
    boolean saved,
    Instant fechaCreacion
) {}
