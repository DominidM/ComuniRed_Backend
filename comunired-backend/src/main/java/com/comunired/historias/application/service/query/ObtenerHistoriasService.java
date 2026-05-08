package com.comunired.historias.application.service.query;

import com.comunired.historias.application.dto.out.HistoriaResponse;
import com.comunired.historias.application.mapper.HistoriaAppMapper;
import com.comunired.historias.application.port.in.ObtenerHistoriasUseCase;
import com.comunired.historias.application.port.out.HistoriaRepositoryPort;
import com.comunired.historias.domain.entity.Historia;
import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObtenerHistoriasService implements ObtenerHistoriasUseCase {

    private final HistoriaRepositoryPort repositoryPort;
    private final HistoriaAppMapper mapper;
    private final UsuariosRepository usuarioRepository;

    public ObtenerHistoriasService(
            HistoriaRepositoryPort repositoryPort,
            HistoriaAppMapper mapper,
            UsuariosRepository usuarioRepository) {
        this.repositoryPort = repositoryPort;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<HistoriaResponse> obtenerActivas(String usuarioSolicitanteId) {
        return repositoryPort.buscarActivas()
            .stream()
            .map(h -> {
                Usuario usuario = usuarioRepository.findById(h.getUsuarioId());
                String nombre = (usuario != null) ? usuario.getNombre() : "Usuario";
                String avatar = (usuario != null) ? usuario.getFoto_perfil() : "";
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

        Usuario usuario = usuarioRepository.findById(historia.getUsuarioId());
        String nombre = (usuario != null) ? usuario.getNombre() : "Usuario";
        String avatar = (usuario != null) ? usuario.getFoto_perfil() : "";

        return mapper.toResponse(historia, true, nombre, avatar);
    }
}