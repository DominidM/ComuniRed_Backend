package com.comunired.reels.application.service.command;

import com.comunired.reels.application.dto.in.CrearReelCommand;
import com.comunired.reels.application.dto.out.ReelResponse;
import com.comunired.reels.application.mapper.ReelAppMapper;
import com.comunired.reels.application.port.in.CrearReelUseCase;
import com.comunired.reels.application.port.out.ReelRepositoryPort;
import com.comunired.reels.application.port.out.ReelStoragePort;
import com.comunired.reels.domain.entity.Reel;
import org.springframework.stereotype.Service;

@Service
public class CrearReelService implements CrearReelUseCase {

    private final ReelRepositoryPort repositoryPort;
    private final ReelAppMapper mapper;
    private final ReelStoragePort storagePort;

    public CrearReelService(ReelRepositoryPort repositoryPort, ReelAppMapper mapper,
                            ReelStoragePort storagePort) {
        this.repositoryPort = repositoryPort;
        this.mapper = mapper;
        this.storagePort = storagePort;
    }

    @Override
    public ReelResponse ejecutar(CrearReelCommand command) {
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
}
