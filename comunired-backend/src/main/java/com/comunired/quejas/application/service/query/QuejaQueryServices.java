package com.comunired.quejas.application.service.query;

import com.comunired.quejas.application.dto.out.QuejaResponses.*;
import com.comunired.quejas.application.mapper.QuejaApplicationMapper;
import com.comunired.quejas.application.port.in.QuejaPorts.*;
import com.comunired.quejas.application.port.out.QuejaOutPorts.*;
import com.comunired.quejas.domain.entity.Queja;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// =============================================================================
// OBTENER POR ID
// =============================================================================
@Service
class ObtenerQuejaService implements ObtenerQuejaUseCase {

    private final QuejaRepositoryPort repository;
    private final QuejaApplicationMapper mapper;

    ObtenerQuejaService(QuejaRepositoryPort repository, QuejaApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public QuejaResponse ejecutar(String quejaId, String usuarioActualId) {
        Queja queja = repository.buscarPorId(quejaId)
                .orElseThrow(() -> new RuntimeException("Queja no encontrada: " + quejaId));
        return mapper.toResponseConContexto(queja, usuarioActualId);
    }
}

// =============================================================================
// LISTAR TODAS
// =============================================================================
@Service
class ListarQuejasService implements ListarQuejasUseCase {

    private final QuejaRepositoryPort repository;
    private final QuejaApplicationMapper mapper;

    ListarQuejasService(QuejaRepositoryPort repository, QuejaApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<QuejaResponse> ejecutar(String usuarioActualId) {
        return repository.buscarTodas().stream()
                .map(q -> mapper.toResponseConContexto(q, usuarioActualId))
                .collect(Collectors.toList());
    }
}

// =============================================================================
// LISTAR PAGINADAS (solo VOTACION — feed público)
// =============================================================================
@Service
class ListarQuejasPaginadasService implements ListarQuejasPaginadasUseCase {

    private final QuejaRepositoryPort repository;
    private final EstadoQuejaPort estadoPort;
    private final QuejaApplicationMapper mapper;

    ListarQuejasPaginadasService(QuejaRepositoryPort repository,
            EstadoQuejaPort estadoPort,
            QuejaApplicationMapper mapper) {
        this.repository = repository;
        this.estadoPort = estadoPort;
        this.mapper = mapper;
    }

    @Override
    public QuejaPageResponse ejecutar(String usuarioActualId, int page, int size) {
        List<Queja> enFeed = repository.buscarTodas().stream()
                .filter(q -> {
                    if (q.getEstadoId() == null) {
                        return false;
                    }
                    return estadoPort.buscarPorId(q.getEstadoId())
                            .map(e -> {
                                String clave = e.clave().toLowerCase();
                                return clave.equals("votacion") || clave.equals("pendiente");
                            })
                            .orElse(false);
                })
                .sorted((a, b) -> {
                    if (a.getFechaCreacion() == null) {
                        return 1;
                    }
                    if (b.getFechaCreacion() == null) {
                        return -1;
                    }
                    return b.getFechaCreacion().compareTo(a.getFechaCreacion());
                })
                .collect(Collectors.toList());

        int total = enFeed.size();
        int start = page * size;
        int end = Math.min(start + size, total);

        List<QuejaResponse> content = (start >= total)
                ? new ArrayList<>()
                : enFeed.subList(start, end).stream()
                        .map(q -> mapper.toResponseConContexto(q, usuarioActualId))
                        .collect(Collectors.toList());

        return new QuejaPageResponse(content, total,
                (int) Math.ceil((double) total / size), page, size, end >= total);
    }

// =============================================================================
// LISTAR POR USUARIO
// =============================================================================
    @Service
    class ListarQuejasPorUsuarioService implements ListarQuejasPorUsuarioUseCase {

        private final QuejaRepositoryPort repository;
        private final QuejaApplicationMapper mapper;

        ListarQuejasPorUsuarioService(QuejaRepositoryPort repository, QuejaApplicationMapper mapper) {
            this.repository = repository;
            this.mapper = mapper;
        }

        @Override
        public List<QuejaResponse> ejecutar(String usuarioId, String usuarioActualId) {
            return repository.buscarPorUsuarioId(usuarioId).stream()
                    .map(q -> mapper.toResponseConContexto(q, usuarioActualId))
                    .collect(Collectors.toList());
        }
    }

// =============================================================================
// LISTAR APROBADAS
// =============================================================================
    @Service
    class ListarQuejasAprobadasService implements ListarQuejasAprobadasUseCase {

        private final QuejaRepositoryPort repository;
        private final QuejaApplicationMapper mapper;

        ListarQuejasAprobadasService(QuejaRepositoryPort repository, QuejaApplicationMapper mapper) {
            this.repository = repository;
            this.mapper = mapper;
        }

        @Override
        public List<QuejaResponse> ejecutar(String usuarioActualId) {
            return repository.buscarAprobadas().stream()
                    .map(q -> mapper.toResponseConContexto(q, usuarioActualId))
                    .collect(Collectors.toList());
        }
    }

// =============================================================================
// LISTAR PARA REVISAR
// =============================================================================
    @Service
    class ListarQuejasParaRevisarService implements ListarQuejasParaRevisarUseCase {

        private final QuejaRepositoryPort repository;
        private final QuejaApplicationMapper mapper;

        ListarQuejasParaRevisarService(QuejaRepositoryPort repository, QuejaApplicationMapper mapper) {
            this.repository = repository;
            this.mapper = mapper;
        }

        @Override
        public List<QuejaResponse> ejecutar(String usuarioActualId) {
            return repository.buscarParaRevisar().stream()
                    .map(q -> mapper.toResponseConContexto(q, usuarioActualId))
                    .collect(Collectors.toList());
        }
    }
}
