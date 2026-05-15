package com.comunired.reels.application.service.command;

import com.comunired.reels.application.dto.in.ActualizarReelCommand;
import com.comunired.reels.application.dto.in.CrearReelCommand;
import com.comunired.reels.application.dto.out.ReelResponse;
import com.comunired.reels.application.mapper.ReelAppMapper;
import com.comunired.reels.application.port.in.GestionarReelUseCase;
import com.comunired.reels.application.port.out.ReelRepositoryPort;
import com.comunired.reels.application.port.out.ReelStoragePort;
import com.comunired.reels.domain.entity.Reel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestionarReelService implements GestionarReelUseCase {

    private final ReelRepositoryPort repositoryPort;
    private final ReelAppMapper mapper;
    private final ReelStoragePort storagePort;

    public GestionarReelService(ReelRepositoryPort repositoryPort, ReelAppMapper mapper,
                                ReelStoragePort storagePort) {
        this.repositoryPort = repositoryPort;
        this.mapper = mapper;
        this.storagePort = storagePort;
    }

    @Override
    public List<ReelResponse> listarTodos(String termino) {
        List<Reel> reels = (termino == null || termino.isBlank())
            ? repositoryPort.buscarTodos()
            : repositoryPort.buscarPorTermino(termino);

        return reels.stream()
            .map(r -> mapper.toResponse(r, false, false))
            .toList();
    }

    @Override
    public ReelResponse obtenerPorId(String id) {
        Reel reel = repositoryPort.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Reel no encontrado: " + id));
        return mapper.toResponse(reel, false, false);
    }

    @Override
    public ReelResponse crear(CrearReelCommand command) {
        String videoUrl = storagePort.subirVideo(command.video());
        Reel reel = Reel.crear(
            videoUrl,
            command.title(),
            command.description(),
            command.authorId(),
            command.author(),
            command.avatarUrl()
        );
        Reel guardado = repositoryPort.guardar(reel);
        return mapper.toResponse(guardado, false, false);
    }

    @Override
    public ReelResponse actualizar(String id, ActualizarReelCommand command) {
        Reel reel = repositoryPort.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Reel no encontrado: " + id));

        if (command.video() != null && !command.video().isEmpty()) {
            String videoUrl = storagePort.subirVideo(command.video());
            reel.setVideoUrl(videoUrl);
        }
        reel.setTitle(command.title());
        reel.setDescription(command.description());
        reel.setAuthor(command.author());
        if (command.avatarUrl() != null) {
            reel.setAvatarUrl(command.avatarUrl());
        }

        Reel guardado = repositoryPort.guardar(reel);
        return mapper.toResponse(guardado, false, false);
    }

    @Override
    public void eliminar(String id) {
        if (repositoryPort.buscarPorId(id).isEmpty()) {
            throw new RuntimeException("Reel no encontrado: " + id);
        }
        repositoryPort.eliminar(id);
    }
}
