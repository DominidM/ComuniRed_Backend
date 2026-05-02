package com.comunired.historias.application.mapper;

import com.comunired.historias.application.dto.out.HistoriaResponse;
import com.comunired.historias.domain.entity.Historia;
import org.springframework.stereotype.Component;

@Component
public class HistoriaAppMapper {

    public HistoriaResponse toResponse(Historia historia, boolean vistaPorMi) {
        return new HistoriaResponse(
            historia.getId(),
            historia.getUsuarioId(),
            historia.getTexto(),
            historia.getImagenUrl(),
            historia.getColorFondo(),
            historia.getDuracion(),
            historia.isActiva(),
            historia.getFechaCreacion(),
            historia.getFechaExpiracion(),
            historia.getVistas().size(),
            vistaPorMi
        );
    }
}