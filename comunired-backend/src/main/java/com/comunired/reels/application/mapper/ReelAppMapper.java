package com.comunired.reels.application.mapper;

import com.comunired.reels.application.dto.out.ReelResponse;
import com.comunired.reels.domain.entity.Reel;
import org.springframework.stereotype.Component;

@Component
public class ReelAppMapper {

    public ReelResponse toResponse(Reel reel, boolean liked, boolean saved) {
        return new ReelResponse(
            reel.getId(),
            reel.getVideoUrl(),
            reel.getTitle(),
            reel.getDescription(),
            reel.getAuthorId(),
            reel.getAuthor(),
            reel.getAvatarUrl(),
            reel.getLikes(),
            reel.getShares(),
            reel.getVistas(),
            reel.getComentariosCount(),
            liked,
            saved,
            reel.getFechaCreacion()
        );
    }
}
