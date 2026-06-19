package com.comunired.historias.infrastructure.mapper;

import com.comunired.historias.domain.entity.Historia;
import com.comunired.historias.infrastructure.adapter.out.persistence.HistoriaDocument;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HistoriaInfraMapper {

    public HistoriaDocument toDocument(Historia historia) {
        HistoriaDocument doc = new HistoriaDocument();
        doc.setId(historia.getId());
        doc.setUsuarioId(historia.getUsuarioId());
        doc.setTexto(historia.getTexto());
        doc.setImagenUrl(historia.getImagenUrl());
        doc.setVideoUrl(historia.getVideoUrl());
        doc.setColorFondo(historia.getColorFondo());
        doc.setDuracion(historia.getDuracion());
        doc.setCancionTitulo(historia.getCancionTitulo());
        doc.setCancionArtista(historia.getCancionArtista());
        doc.setCancionPreviewUrl(historia.getCancionPreviewUrl());
        doc.setCancionCoverUrl(historia.getCancionCoverUrl());
        doc.setActiva(historia.isActiva());
        doc.setFechaCreacion(historia.getFechaCreacion());
        doc.setFechaExpiracion(historia.getFechaExpiracion());
        doc.setVistas(historia.getVistas().stream()
            .map(v -> {
                HistoriaDocument.VistaDocument vd = new HistoriaDocument.VistaDocument();
                vd.setUsuarioId(v.usuarioId());
                vd.setFechaVista(v.fechaVista());
                return vd;
            })
            .collect(Collectors.toList()));
        doc.setLikes(historia.getLikes().stream()
            .map(l -> {
                HistoriaDocument.LikeDocument ld = new HistoriaDocument.LikeDocument();
                ld.setUsuarioId(l.usuarioId());
                ld.setFechaLike(l.fechaLike());
                return ld;
            })
            .collect(Collectors.toList()));
        doc.setRespuestas(historia.getRespuestas().stream()
            .map(r -> {
                HistoriaDocument.RespuestaDocument rd = new HistoriaDocument.RespuestaDocument();
                rd.setUsuarioId(r.usuarioId());
                rd.setTexto(r.texto());
                rd.setFechaRespuesta(r.fechaRespuesta());
                return rd;
            })
            .collect(Collectors.toList()));
        return doc;
    }

    public Historia toDomain(HistoriaDocument doc) {
        Historia historia = Historia.reconstruir(
            doc.getId(),
            doc.getUsuarioId(),
            doc.getTexto(),
            doc.getImagenUrl(),
            doc.getVideoUrl(),
            doc.getColorFondo(),
            doc.getDuracion(),
            doc.getCancionTitulo(),
            doc.getCancionArtista(),
            doc.getCancionPreviewUrl(),
            doc.getCancionCoverUrl(),
            doc.isActiva(),
            doc.getFechaCreacion(),
            doc.getFechaExpiracion(),
            doc.getVistas().stream()
                .map(v -> new Historia.Vista(v.getUsuarioId(), v.getFechaVista()))
                .collect(Collectors.toList()),
            doc.getLikes().stream()
                .map(l -> new Historia.Like(l.getUsuarioId(), l.getFechaLike()))
                .collect(Collectors.toList()),
            doc.getRespuestas().stream()
                .map(r -> new Historia.Respuesta(r.getUsuarioId(), r.getTexto(), r.getFechaRespuesta()))
                .collect(Collectors.toList())
        );
        return historia;
    }
}