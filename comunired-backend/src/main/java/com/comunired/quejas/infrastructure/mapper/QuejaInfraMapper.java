package com.comunired.quejas.infrastructure.mapper;

import com.comunired.quejas.domain.entity.Queja;
import com.comunired.quejas.infrastructure.adapter.out.persistence.QuejaDocument;
import org.springframework.stereotype.Component;

/**
 * Mapper de infraestructura: Queja (dominio) ↔ QuejaDocument (MongoDB).
 * Manual, sin MapStruct — el dominio no puede tener anotaciones de framework.
 */
@Component
public class QuejaInfraMapper {

    public QuejaDocument toDocument(Queja queja) {
        QuejaDocument doc = new QuejaDocument();
        doc.setId(queja.getId());
        doc.setTitulo(queja.getTitulo());
        doc.setDescripcion(queja.getDescripcion());
        doc.setUsuarioId(queja.getUsuarioId());
        doc.setCategoriaId(queja.getCategoriaId());
        doc.setEstadoId(queja.getEstadoId());
        doc.setUbicacion(queja.getUbicacion());
        doc.setImagenUrl(queja.getImagenUrl());
        doc.setFechaCreacion(queja.getFechaCreacion());
        doc.setFechaActualizacion(queja.getFechaActualizacion());
        doc.setNivelRiesgo(queja.getNivelRiesgo());
        doc.setFechaClasificacion(queja.getFechaClasificacion());
        doc.setClasificadoPorId(queja.getClasificadoPorId());
        doc.setFechaAprobacion(queja.getFechaAprobacion());
        doc.setLat(queja.getLat());
        doc.setLng(queja.getLng());
        return doc;
    }

    public Queja toDomain(QuejaDocument doc) {
        return Queja.reconstruir(
                doc.getId(),
                doc.getTitulo(),
                doc.getDescripcion(),
                doc.getUsuarioId(),
                doc.getCategoriaId(),
                doc.getEstadoId(),
                doc.getUbicacion(),
                doc.getImagenUrl(),
                doc.getFechaCreacion(),
                doc.getFechaActualizacion(),
                doc.getNivelRiesgo(),
                doc.getFechaClasificacion(),
                doc.getClasificadoPorId(),
                doc.getFechaAprobacion(),
                doc.getLat(),
                doc.getLng()
        );
    }
}
