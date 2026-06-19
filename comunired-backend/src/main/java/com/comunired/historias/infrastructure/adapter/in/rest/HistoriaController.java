package com.comunired.historias.infrastructure.adapter.in.rest;

import com.comunired.historias.application.dto.in.CrearHistoriaCommand;
import com.comunired.historias.application.dto.in.ReplyRequest;
import com.comunired.historias.application.dto.out.HistoriaResponse;
import com.comunired.historias.application.dto.out.ViewerResponse;
import com.comunired.historias.application.port.in.CrearHistoriaUseCase;
import com.comunired.historias.application.port.in.EliminarHistoriaUseCase;
import com.comunired.historias.application.port.in.ObtenerHistoriasUseCase;
import com.comunired.historias.application.port.out.HistoriaRepositoryPort;
import com.comunired.historias.domain.entity.Historia;
import com.comunired.mensajeria.application.service.MensajeService;
import com.comunired.mensajeria.domain.entity.Conversacion;
import com.comunired.mensajeria.domain.repository.ConversacionRepository;
import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historias")
public class HistoriaController {

    private final CrearHistoriaUseCase crearHistoriaUseCase;
    private final ObtenerHistoriasUseCase obtenerHistoriasUseCase;
    private final EliminarHistoriaUseCase eliminarHistoriaUseCase;
    private final HistoriaRepositoryPort historiaRepository;
    private final UsuariosRepository usuariosRepository;
    private final ConversacionRepository conversacionRepository;
    private final MensajeService mensajeService;

    public HistoriaController(
        CrearHistoriaUseCase crearHistoriaUseCase,
        ObtenerHistoriasUseCase obtenerHistoriasUseCase,
        EliminarHistoriaUseCase eliminarHistoriaUseCase,
        HistoriaRepositoryPort historiaRepository,
        UsuariosRepository usuariosRepository,
        ConversacionRepository conversacionRepository,
        MensajeService mensajeService
    ) {
        this.crearHistoriaUseCase = crearHistoriaUseCase;
        this.obtenerHistoriasUseCase = obtenerHistoriasUseCase;
        this.eliminarHistoriaUseCase = eliminarHistoriaUseCase;
        this.historiaRepository = historiaRepository;
        this.usuariosRepository = usuariosRepository;
        this.conversacionRepository = conversacionRepository;
        this.mensajeService = mensajeService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HistoriaResponse> crear(
        @RequestParam String usuarioId,
        @RequestParam(required = false) String texto,
        @RequestParam(required = false) String colorFondo,
        @RequestParam(defaultValue = "5") int duracion,
        @RequestParam(required = false) MultipartFile imagen,
        @RequestParam(required = false) MultipartFile video,
        @RequestParam(required = false) String cancionTitulo,
        @RequestParam(required = false) String cancionArtista,
        @RequestParam(required = false) String cancionPreviewUrl,
        @RequestParam(required = false) String cancionCoverUrl
    ) {
        CrearHistoriaCommand command = new CrearHistoriaCommand(
            usuarioId, texto, colorFondo, duracion, imagen, video,
            cancionTitulo, cancionArtista, cancionPreviewUrl, cancionCoverUrl
        );
        return ResponseEntity.ok(crearHistoriaUseCase.ejecutar(command));
    }

    @GetMapping
    public ResponseEntity<List<HistoriaResponse>> obtenerActivas(
        @RequestParam String usuarioId
    ) {
        return ResponseEntity.ok(obtenerHistoriasUseCase.obtenerActivas(usuarioId));
    }

    @PatchMapping("/{id}/vista")
    public ResponseEntity<HistoriaResponse> marcarVista(
        @PathVariable String id,
        @RequestParam String usuarioId
    ) {
        return ResponseEntity.ok(obtenerHistoriasUseCase.marcarVista(id, usuarioId));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable String id,
            @RequestParam String usuarioId
    ) {
        Historia historia = historiaRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));
        historia.toggleLike(usuarioId);
        historiaRepository.guardarActualizada(historia);
        return ResponseEntity.ok(Map.of(
            "leGusta", historia.leGusta(usuarioId),
            "totalLikes", historia.getLikes().size()
        ));
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<Void> responder(
            @PathVariable String id,
            @RequestBody ReplyRequest request
    ) {
        Historia historia = historiaRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));
        String creadorId = historia.getUsuarioId();
        String viewerId = request.usuarioId();
        Conversacion conversacion = conversacionRepository
                .findByParticipantes(viewerId, creadorId)
                .orElseGet(() -> {
                    Conversacion nueva = new Conversacion();
                    nueva.setParticipante1Id(viewerId);
                    nueva.setParticipante2Id(creadorId);
                    return conversacionRepository.save(nueva);
                });
        mensajeService.enviarMensaje(conversacion.getId(), viewerId, request.texto());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/viewers")
    public ResponseEntity<List<ViewerResponse>> obtenerVisualizadores(@PathVariable String id) {
        Historia historia = historiaRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));
        List<ViewerResponse> viewers = historia.getVistas().stream()
                .filter(v -> !v.usuarioId().equals(historia.getUsuarioId()))
                .map(v -> {
                    Usuario u = usuariosRepository.findById(v.usuarioId());
                    return new ViewerResponse(
                        v.usuarioId(),
                        u != null ? u.getNombre() : "Usuario",
                        u != null ? u.getFoto_perfil() : "",
                        historia.leGusta(v.usuarioId())
                    );
                })
                .toList();
        return ResponseEntity.ok(viewers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        eliminarHistoriaUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }
}