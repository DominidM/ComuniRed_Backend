package com.comunired.quejas.application.service.command;

import com.comunired.quejas.application.dto.in.QuejaCommands.*;
import com.comunired.quejas.application.dto.out.QuejaResponses.*;
import com.comunired.quejas.application.mapper.QuejaApplicationMapper;
import com.comunired.quejas.application.port.in.QuejaPorts.*;
import com.comunired.quejas.application.port.out.QuejaOutPorts.*;
import com.comunired.quejas.domain.entity.Queja;
import com.comunired.quejas.domain.event.QuejaEvents.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;

// =============================================================================
// CREAR QUEJA
// =============================================================================
@Service
class CrearQuejaService implements CrearQuejaUseCase {

    private final QuejaRepositoryPort repository;
    private final EstadoQuejaPort estadoPort;
    private final QuejaApplicationMapper mapper;
    private final ApplicationEventPublisher events;

    CrearQuejaService(QuejaRepositoryPort repository,
                      EstadoQuejaPort estadoPort,
                      QuejaApplicationMapper mapper,
                      ApplicationEventPublisher events) {
        this.repository = repository;
        this.estadoPort = estadoPort;
        this.mapper = mapper;
        this.events = events;
    }

    @Override
    public QuejaResponse ejecutar(CrearQuejaCommand command) {
        String estadoId = estadoPort.buscarPorClave("VOTACION")
                .map(EstadoQuejaPort.EstadoInfo::id)
                .orElseThrow(() -> new IllegalStateException("Estado VOTACION no configurado en el sistema"));

        Queja queja = Queja.crear(
                command.titulo(),
                command.descripcion(),
                command.usuarioId(),
                command.categoriaId(),
                command.ubicacion(),
                estadoId
        );

        if (command.imagenUrl() != null) {
            queja.asignarImagen(command.imagenUrl());
        }

        Queja guardada = repository.guardar(queja);

        events.publishEvent(new QuejaCreada(
                guardada.getId(),
                command.usuarioId(),
                command.categoriaId(),
                command.titulo(),
                guardada.getFechaCreacion()
        ));

        return mapper.toResponse(guardada);
    }
}

// =============================================================================
// ACTUALIZAR QUEJA
// =============================================================================
@Service
class ActualizarQuejaService implements ActualizarQuejaUseCase {

    private final QuejaRepositoryPort repository;
    private final QuejaApplicationMapper mapper;

    ActualizarQuejaService(QuejaRepositoryPort repository, QuejaApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public QuejaResponse ejecutar(ActualizarQuejaCommand command) {
        Queja queja = repository.buscarPorId(command.id())
                .orElseThrow(() -> new RuntimeException("Queja no encontrada: " + command.id()));

        queja.actualizarDatos(
                command.titulo(),
                command.descripcion(),
                command.categoriaId(),
                command.ubicacion(),
                command.imagenUrl()
        );

        return mapper.toResponse(repository.guardar(queja));
    }
}

// =============================================================================
// CLASIFICAR RIESGO
// =============================================================================
@Service
class ClasificarRiesgoService implements ClasificarRiesgoUseCase {

    private final QuejaRepositoryPort repository;
    private final EstadoQuejaPort estadoPort;
    private final QuejaApplicationMapper mapper;
    private final ApplicationEventPublisher events;

    ClasificarRiesgoService(QuejaRepositoryPort repository,
                             EstadoQuejaPort estadoPort,
                             QuejaApplicationMapper mapper,
                             ApplicationEventPublisher events) {
        this.repository = repository;
        this.estadoPort = estadoPort;
        this.mapper = mapper;
        this.events = events;
    }

    @Override
    public QuejaResponse ejecutar(ClasificarRiesgoCommand command) {
        Queja queja = repository.buscarPorId(command.quejaId())
                .orElseThrow(() -> new RuntimeException("Queja no encontrada: " + command.quejaId()));

        // La validación del nivel está en el dominio
        queja.clasificarRiesgo(command.nivelRiesgo(), command.soporteId());

        estadoPort.buscarPorClave("CLASIFICADA").ifPresent(e -> queja.cambiarEstado(e.id()));

        Queja updated = repository.guardar(queja);

        events.publishEvent(new QuejaClasificada(
                command.quejaId(),
                command.soporteId(),
                command.nivelRiesgo(),
                command.observacion(),
                Instant.now()
        ));

        return mapper.toResponse(updated);
    }
}

// =============================================================================
// CAMBIAR ESTADO
// =============================================================================
@Service
class CambiarEstadoQuejaService implements CambiarEstadoQuejaUseCase {

    private final QuejaRepositoryPort repository;
    private final EstadoQuejaPort estadoPort;
    private final QuejaApplicationMapper mapper;
    private final ApplicationEventPublisher events;

    CambiarEstadoQuejaService(QuejaRepositoryPort repository,
                               EstadoQuejaPort estadoPort,
                               QuejaApplicationMapper mapper,
                               ApplicationEventPublisher events) {
        this.repository = repository;
        this.estadoPort = estadoPort;
        this.mapper = mapper;
        this.events = events;
    }

    @Override
    public QuejaResponse ejecutar(CambiarEstadoCommand command) {
        Queja queja = repository.buscarPorId(command.quejaId())
                .orElseThrow(() -> new RuntimeException("Queja no encontrada: " + command.quejaId()));

        String estadoAnteriorClave = estadoPort.buscarPorId(queja.getEstadoId())
                .map(EstadoQuejaPort.EstadoInfo::clave)
                .orElse(null);

        EstadoQuejaPort.EstadoInfo nuevoEstado = estadoPort.buscarPorClave(command.nuevoEstadoClave())
                .orElseThrow(() -> new RuntimeException("Estado no válido: " + command.nuevoEstadoClave()));

        if ("APROBADA".equalsIgnoreCase(command.nuevoEstadoClave())) {
            queja.aprobar(nuevoEstado.id());
        } else {
            queja.cambiarEstado(nuevoEstado.id());
        }

        Queja updated = repository.guardar(queja);

        events.publishEvent(new QuejaEstadoCambiado(
                command.quejaId(),
                command.usuarioId(),
                estadoAnteriorClave,
                command.nuevoEstadoClave(),
                command.observacion(),
                Instant.now()
        ));

        return mapper.toResponse(updated);
    }
}

// =============================================================================
// ELIMINAR QUEJA
// =============================================================================
@Service
class EliminarQuejaService implements EliminarQuejaUseCase {

    private final QuejaRepositoryPort repository;
    private final ApplicationEventPublisher events;

    EliminarQuejaService(QuejaRepositoryPort repository, ApplicationEventPublisher events) {
        this.repository = repository;
        this.events = events;
    }

    @Override
    public boolean ejecutar(EliminarQuejaCommand command) {
        if (!repository.existePorId(command.quejaId())) return false;
        repository.eliminar(command.quejaId());
        events.publishEvent(new QuejaEliminada(command.quejaId(), command.usuarioId(), Instant.now()));
        return true;
    }
}
