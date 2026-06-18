package com.comunired.historias.application.mapper;

import com.comunired.historias.application.dto.out.HistoriaResponse;
import com.comunired.historias.domain.entity.Historia;
import org.springframework.stereotype.Component;

@Component
public class HistoriaAppMapper {

    public HistoriaResponse toResponse(Historia historia, boolean vistaPorMi,
                                        String userName, String userAvatar) {
        return new HistoriaResponse(
            historia.getId(),
            historia.getUsuarioId(),
            userName,
            userAvatar,
            historia.getTexto(),
            historia.getImagenUrl(),
            historia.getVideoUrl(),
            historia.getColorFondo(),
            historia.getDuracion(),
            historia.getCancionTitulo(),
            historia.getCancionArtista(),
            historia.getCancionPreviewUrl(),
            historia.getCancionCoverUrl(),
            historia.isActiva(),
            historia.getFechaCreacion(),
            historia.getFechaExpiracion(),
            historia.getVistas().size(),
            vistaPorMi
        );
    }
}