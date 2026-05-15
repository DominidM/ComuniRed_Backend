package com.comunired.reels.application.service.query;

import com.comunired.reels.application.dto.out.ReelComentarioResponse;
import com.comunired.reels.application.dto.out.ReelResponse;
import com.comunired.reels.application.mapper.ReelAppMapper;
import com.comunired.reels.application.port.in.ObtenerReelsUseCase;
import com.comunired.reels.application.port.out.ReelRepositoryPort;
import com.comunired.reels.domain.entity.Reel;
import com.comunired.reels.infrastructure.adapter.out.persistence.ReelComentarioDocument;
import com.comunired.reels.infrastructure.adapter.out.persistence.ReelComentarioMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObtenerReelsService implements ObtenerReelsUseCase {

    private final ReelRepositoryPort repositoryPort;
    private final ReelAppMapper mapper;
    private final ReelComentarioMongoRepository comentarioRepo;

    public ObtenerReelsService(ReelRepositoryPort repositoryPort, ReelAppMapper mapper,
                               ReelComentarioMongoRepository comentarioRepo) {
        this.repositoryPort = repositoryPort;
        this.mapper = mapper;
        this.comentarioRepo = comentarioRepo;
    }

    @Override
    public List<ReelResponse> obtenerActivos(String usuarioSolicitanteId) {
        return repositoryPort.buscarActivos()
            .stream()
            .map(r -> {
                boolean liked = r.fueLikedPor(usuarioSolicitanteId);
                boolean saved = r.fueSavedPor(usuarioSolicitanteId);
                return mapper.toResponse(r, liked, saved);
            })
            .toList();
    }

    @Override
    public ReelResponse marcarLike(String reelId, String usuarioId) {
        Reel reel = repositoryPort.buscarPorId(reelId)
            .orElseThrow(() -> new RuntimeException("Reel no encontrado: " + reelId));

        boolean liked = reel.toggleLike(usuarioId);
        repositoryPort.guardar(reel);

        return mapper.toResponse(reel, liked, reel.fueSavedPor(usuarioId));
    }

    @Override
    public ReelResponse marcarSave(String reelId, String usuarioId) {
        Reel reel = repositoryPort.buscarPorId(reelId)
            .orElseThrow(() -> new RuntimeException("Reel no encontrado: " + reelId));

        boolean saved = reel.toggleSave(usuarioId);
        repositoryPort.guardar(reel);

        return mapper.toResponse(reel, reel.fueLikedPor(usuarioId), saved);
    }

    @Override
    public ReelResponse incrementarVista(String reelId) {
        Reel reel = repositoryPort.buscarPorId(reelId)
            .orElseThrow(() -> new RuntimeException("Reel no encontrado: " + reelId));

        reel.incrementarVista();
        repositoryPort.guardar(reel);

        return mapper.toResponse(reel, false, false);
    }

    @Override
    public ReelComentarioResponse comentar(String reelId, String usuarioId, String usuarioNombre, String usuarioAvatar, String texto) {
        Reel reel = repositoryPort.buscarPorId(reelId)
            .orElseThrow(() -> new RuntimeException("Reel no encontrado: " + reelId));

        ReelComentarioDocument doc = new ReelComentarioDocument(reelId, usuarioId, usuarioNombre, usuarioAvatar, texto);
        ReelComentarioDocument saved = comentarioRepo.save(doc);

        reel.setComentariosCount(comentarioRepo.countByReelId(reelId));
        repositoryPort.guardar(reel);

        return new ReelComentarioResponse(
            saved.getId(), saved.getReelId(), saved.getUsuarioId(),
            saved.getUsuarioNombre(), saved.getUsuarioAvatar(),
            saved.getTexto(), saved.getFechaCreacion()
        );
    }

    @Override
    public List<ReelComentarioResponse> obtenerComentarios(String reelId) {
        return comentarioRepo.findByReelIdOrderByFechaCreacionAsc(reelId)
            .stream()
            .map(c -> new ReelComentarioResponse(
                c.getId(), c.getReelId(), c.getUsuarioId(),
                c.getUsuarioNombre(), c.getUsuarioAvatar(),
                c.getTexto(), c.getFechaCreacion()
            ))
            .toList();
    }
}
