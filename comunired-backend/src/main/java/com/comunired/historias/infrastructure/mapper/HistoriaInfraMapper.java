package com.comunired.historias.infrastructure.mapper;

import com.comunired.historias.domain.entity.Historia;
import com.comunired.historias.infrastructure.adapter.out.persistence.HistoriaDocument;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HistoriaInfraMapper {

    public HistoriaDocument toDocument(Historia historia) {
        HistoriaDocument doc = new HistoriaDocument();
        doc.setUsuarioId(historia.getUsuarioId());
        doc.setTexto(historia.getTexto());
        doc.setImagenUrl(historia.getImagenUrl());
        doc.setColorFondo(historia.getColorFondo());
        doc.setDuracion(historia.getDuracion());
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
        return doc;
    }

    public Historia toDomain(HistoriaDocument doc) {
        Historia historia = Historia.reconstruir(
            doc.getId(),
            doc.getUsuarioId(),
            doc.getTexto(),
            doc.getImagenUrl(),
            doc.getColorFondo(),
            doc.getDuracion(),
            doc.isActiva(),
            doc.getFechaCreacion(),
            doc.getFechaExpiracion(),
            doc.getVistas().stream()
                .map(v -> new Historia.Vista(v.getUsuarioId(), v.getFechaVista()))
                .collect(Collectors.toList())
        );
        return historia;
    }
}