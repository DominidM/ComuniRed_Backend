package com.comunired.historias.application.service.command;

import com.comunired.historias.application.dto.in.CrearHistoriaCommand;
import com.comunired.historias.application.dto.out.HistoriaResponse;
import com.comunired.historias.application.mapper.HistoriaAppMapper;
import com.comunired.historias.application.port.in.CrearHistoriaUseCase;
import com.comunired.historias.application.port.out.HistoriaRepositoryPort;
import com.comunired.historias.application.port.out.StoragePort;
import com.comunired.historias.domain.entity.Historia;
import com.comunired.historias.domain.event.HistoriaPublicada;
import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CrearHistoriaService implements CrearHistoriaUseCase {

    private final HistoriaRepositoryPort repositoryPort;
    private final StoragePort storagePort;
    private final HistoriaAppMapper mapper;
    private final ApplicationEventPublisher eventPublisher;
    private final UsuariosRepository usuariosRepository;

    public CrearHistoriaService(
            HistoriaRepositoryPort repositoryPort,
            StoragePort storagePort,
            HistoriaAppMapper mapper,
            ApplicationEventPublisher eventPublisher,
            UsuariosRepository usuariosRepository) {
        this.repositoryPort = repositoryPort;
        this.storagePort = storagePort;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public HistoriaResponse ejecutar(CrearHistoriaCommand command) {
        Optional<Historia> existente = repositoryPort.buscarDuplicado(
                command.usuarioId(), command.texto(), command.colorFondo());
        if (existente.isPresent()) {
            Usuario usuario = usuariosRepository.findById(existente.get().getUsuarioId());
            return mapper.toResponse(existente.get(), false,
                    usuario != null ? usuario.getNombre() : "Usuario",
                    usuario != null ? usuario.getFoto_perfil() : "");
        }

        String imagenUrl = null;
        if (command.imagen() != null && !command.imagen().isEmpty()) {
            imagenUrl = storagePort.subir(command.imagen(), "historias");
        }

        String videoUrl = null;
        if (command.video() != null && !command.video().isEmpty()) {
            videoUrl = storagePort.subir(command.video(), "historias");
        }

        Historia historia = Historia.crear(
                command.usuarioId(),
                command.texto(),
                imagenUrl,
                videoUrl,
                command.colorFondo(),
                command.duracion(),
                command.cancionTitulo(),
                command.cancionArtista(),
                command.cancionPreviewUrl(),
                command.cancionCoverUrl()
        );

        Historia guardada = repositoryPort.guardar(historia);

        eventPublisher.publishEvent(new HistoriaPublicada(
                guardada.getId(),
                guardada.getUsuarioId(),
                null,
                guardada.getFechaCreacion()
        ));

        Usuario usuario = usuariosRepository.findById(guardada.getUsuarioId());
        String nombre = (usuario != null) ? usuario.getNombre() : "Usuario";
        String avatar = (usuario != null) ? usuario.getFoto_perfil() : "";

        return mapper.toResponse(guardada, false, nombre, avatar);
    }
}