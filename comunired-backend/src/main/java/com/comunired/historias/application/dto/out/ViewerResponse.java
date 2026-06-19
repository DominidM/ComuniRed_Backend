package com.comunired.historias.application.dto.out;

public record ViewerResponse(
    String usuarioId,
    String userName,
    String userAvatar,
    boolean leGusta
) {}
