package com.comunired.quejas.application.service.query;

import com.comunired.quejas.application.dto.out.QuejaResponses.*;
import com.comunired.quejas.application.mapper.QuejaApplicationMapper;
import com.comunired.quejas.application.port.in.QuejaPorts.*;
import com.comunired.quejas.application.port.out.QuejaOutPorts.*;
import com.comunired.quejas.domain.entity.Queja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return mapper.toResponseBatchConContexto(repository.buscarTodas(), usuarioActualId);
    }
}

@Service
class ListarQuejasPaginadasService implements ListarQuejasPaginadasUseCase {

    private final QuejaRepositoryPort repository;
    private final QuejaApplicationMapper mapper;

    private static final List<String> CLAVES_FEED = List.of("votacion", "pendiente", "VOTACION", "PENDIENTE");

    ListarQuejasPaginadasService(QuejaRepositoryPort repository, QuejaApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public QuejaPageResponse ejecutar(String usuarioActualId, int page, int size) {
        Page<Queja> pageResult = repository.buscarPorEstadosClavePaginado(CLAVES_FEED, page, size);

        List<QuejaResponse> content = mapper.toResponseBatchConContexto(
                pageResult.getContent(), usuarioActualId);

        return new QuejaPageResponse(
                content,
                (int) pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                page,
                size,
                pageResult.isLast()
        );
    }
}

@Service
class ListarQuejasAdminPaginadasService implements ListarQuejasAdminPaginadasUseCase {

    private final QuejaRepositoryPort repository;
    private final QuejaApplicationMapper mapper;

    ListarQuejasAdminPaginadasService(QuejaRepositoryPort repository, QuejaApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public QuejaPageResponse ejecutar(String usuarioActualId, int page, int size) {
        Page<Queja> pageResult = repository.buscarTodasPaginado(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaCreacion")));

        List<QuejaResponse> content = mapper.toResponseBatch(
                pageResult.getContent(), usuarioActualId);

        return new QuejaPageResponse(
                content,
                (int) pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                page,
                size,
                pageResult.isLast()
        );
    }
}

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
        return mapper.toResponseBatchConContexto(repository.buscarPorUsuarioId(usuarioId), usuarioActualId);
    }
}

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
        return mapper.toResponseBatchConContexto(repository.buscarAprobadas(), usuarioActualId);
    }
}

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
        return mapper.toResponseBatchConContexto(repository.buscarParaRevisar(), usuarioActualId);
    }
}