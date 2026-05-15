package com.comunired.reels.infrastructure.mapper;

import com.comunired.reels.domain.entity.Reel;
import com.comunired.reels.infrastructure.adapter.out.persistence.ReelDocument;
import org.springframework.stereotype.Component;

@Component
public class ReelInfraMapper {

    public ReelDocument toDocument(Reel reel) {
        ReelDocument doc = new ReelDocument();
        doc.setId(reel.getId());
        doc.setVideoUrl(reel.getVideoUrl());
        doc.setTitle(reel.getTitle());
        doc.setDescription(reel.getDescription());
        doc.setAuthorId(reel.getAuthorId());
        doc.setAuthor(reel.getAuthor());
        doc.setAvatarUrl(reel.getAvatarUrl());
        doc.setLikes(reel.getLikes());
        doc.setShares(reel.getShares());
        doc.setVistas(reel.getVistas());
        doc.setComentariosCount(reel.getComentariosCount());
        doc.setActivo(reel.isActivo());
        doc.setFechaCreacion(reel.getFechaCreacion());
        doc.setFechaExpiracion(reel.getFechaExpiracion());
        doc.setLikedBy(reel.getLikedBy());
        doc.setSavedBy(reel.getSavedBy());
        return doc;
    }

    public Reel toDomain(ReelDocument doc) {
        return Reel.reconstruir(
            doc.getId(),
            doc.getVideoUrl(),
            doc.getTitle(),
            doc.getDescription(),
            doc.getAuthorId(),
            doc.getAuthor(),
            doc.getAvatarUrl(),
            doc.getLikes(),
            doc.getShares(),
            doc.getVistas(),
            doc.getComentariosCount(),
            doc.isActivo(),
            doc.getFechaCreacion(),
            doc.getFechaExpiracion(),
            doc.getLikedBy(),
            doc.getSavedBy()
        );
    }
}
