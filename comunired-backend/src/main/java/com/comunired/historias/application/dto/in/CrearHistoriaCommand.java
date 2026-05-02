package com.comunired.historias.application.dto.in;

import org.springframework.web.multipart.MultipartFile;

public record CrearHistoriaCommand(
    String usuarioId,
    String texto,
    String colorFondo,
    int duracion,
    MultipartFile imagen
) {}