package com.comunired.historias.application.service.query;

import com.comunired.historias.application.dto.out.HistoriaResponse;
import com.comunired.historias.application.mapper.HistoriaAppMapper;
import com.comunired.historias.application.port.in.ObtenerHistoriasUseCase;
import com.comunired.historias.application.port.out.HistoriaRepositoryPort;
import com.comunired.historias.domain.entity.Historia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ObtenerHistoriasService implements ObtenerHistoriasUseCase {

    private final HistoriaRepositoryPort repositoryPort;
    private final HistoriaAppMapper mapper;

    @Override
    public List<HistoriaResponse> obtenerActivas(String usuarioSolicitanteId) {
        return repositoryPort.buscarActivas()
            .stream()
            .map(h -> mapper.toResponse(h, h.fueVistaPor(usuarioSolicitanteId)))
            .toList();
    }

    @Override
    public HistoriaResponse marcarVista(String historiaId, String usuarioId) {
        Historia historia = repositoryPort.buscarPorId(historiaId)
            .orElseThrow(() -> new RuntimeException("Historia no encontrada: " + historiaId));

        historia.marcarVista(usuarioId);
        repositoryPort.guardarActualizada(historia);

        return mapper.toResponse(historia, true);
    }
}