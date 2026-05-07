package com.comunired.historias.application.service.query;

import com.comunired.historias.application.dto.out.HistoriaResponse;
import com.comunired.historias.application.mapper.HistoriaAppMapper;
import com.comunired.historias.application.port.in.ObtenerHistoriasUseCase;
import com.comunired.historias.application.port.out.HistoriaRepositoryPort;
import com.comunired.historias.domain.entity.Historia;
import com.comunired.usuarios.domain.repository.UsuarioRepository; // ← ajusta si el nombre difiere
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ObtenerHistoriasService implements ObtenerHistoriasUseCase {

    private final HistoriaRepositoryPort repositoryPort;
    private final HistoriaAppMapper mapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<HistoriaResponse> obtenerActivas(String usuarioSolicitanteId) {
        return repositoryPort.buscarActivas()
            .stream()
            .map(h -> {
                var usuario = usuarioRepository.findById(h.getUsuarioId());
                String nombre = usuario.map(u -> u.getNombre()).orElse("Usuario");
                String avatar = usuario.map(u -> u.getAvatarUrl()).orElse("");
                return mapper.toResponse(h, h.fueVistaPor(usuarioSolicitanteId), nombre, avatar);
            })
            .toList();
    }

    @Override
    public HistoriaResponse marcarVista(String historiaId, String usuarioId) {
        Historia historia = repositoryPort.buscarPorId(historiaId)
            .orElseThrow(() -> new RuntimeException("Historia no encontrada: " + historiaId));

        historia.marcarVista(usuarioId);
        repositoryPort.guardarActualizada(historia);

        var usuario = usuarioRepository.findById(historia.getUsuarioId());
        String nombre = usuario.map(u -> u.getNombre()).orElse("Usuario");
        String avatar = usuario.map(u -> u.getAvatarUrl()).orElse("");

        return mapper.toResponse(historia, true, nombre, avatar);
    }
}