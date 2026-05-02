package com.comunired.historias.application.service.command;

import com.comunired.historias.application.dto.in.CrearHistoriaCommand;
import com.comunired.historias.application.dto.out.HistoriaResponse;
import com.comunired.historias.application.mapper.HistoriaAppMapper;
import com.comunired.historias.application.port.in.CrearHistoriaUseCase;
import com.comunired.historias.application.port.out.HistoriaRepositoryPort;
import com.comunired.historias.application.port.out.StoragePort;
import com.comunired.historias.domain.entity.Historia;
import com.comunired.historias.domain.event.HistoriaPublicada;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrearHistoriaService implements CrearHistoriaUseCase {

    private final HistoriaRepositoryPort repositoryPort;
    private final StoragePort storagePort;
    private final HistoriaAppMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public HistoriaResponse ejecutar(CrearHistoriaCommand command) {
        String imagenUrl = null;
        if (command.imagen() != null && !command.imagen().isEmpty()) {
            imagenUrl = storagePort.subir(command.imagen(), "historias");
        }

        Historia historia = Historia.crear(
            command.usuarioId(),
            command.texto(),
            imagenUrl,
            command.colorFondo(),
            command.duracion()
        );

        Historia guardada = repositoryPort.guardar(historia);

        eventPublisher.publishEvent(new HistoriaPublicada(
            guardada.getId(),
            guardada.getUsuarioId(),
            null, // distrito se puede enriquecer después
            guardada.getFechaCreacion()
        ));

        return mapper.toResponse(guardada, false);
    }
}