package com.comunired.reels.application.port.in;

import com.comunired.reels.application.dto.out.ReelComentarioResponse;
import com.comunired.reels.application.dto.out.ReelResponse;

import java.util.List;

public interface ObtenerReelsUseCase {
    List<ReelResponse> obtenerActivos(String usuarioSolicitanteId);
    ReelResponse marcarLike(String reelId, String usuarioId);
    ReelResponse marcarSave(String reelId, String usuarioId);
    ReelResponse incrementarVista(String reelId);
    ReelComentarioResponse comentar(String reelId, String usuarioId, String usuarioNombre, String usuarioAvatar, String texto);
    List<ReelComentarioResponse> obtenerComentarios(String reelId);
}
