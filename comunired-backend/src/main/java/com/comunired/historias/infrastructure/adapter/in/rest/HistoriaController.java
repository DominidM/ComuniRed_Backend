package com.comunired.historias.infrastructure.adapter.in.rest;

import com.comunired.historias.application.dto.in.CrearHistoriaCommand;
import com.comunired.historias.application.dto.out.HistoriaResponse;
import com.comunired.historias.application.port.in.CrearHistoriaUseCase;
import com.comunired.historias.application.port.in.ObtenerHistoriasUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/historias")
public class HistoriaController {

    private final CrearHistoriaUseCase crearHistoriaUseCase;
    private final ObtenerHistoriasUseCase obtenerHistoriasUseCase;

    public HistoriaController(
        CrearHistoriaUseCase crearHistoriaUseCase,
        ObtenerHistoriasUseCase obtenerHistoriasUseCase
    ) {
        this.crearHistoriaUseCase = crearHistoriaUseCase;
        this.obtenerHistoriasUseCase = obtenerHistoriasUseCase;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HistoriaResponse> crear(
        @RequestParam String usuarioId,
        @RequestParam(required = false) String texto,
        @RequestParam(required = false) String colorFondo,
        @RequestParam(defaultValue = "5") int duracion,
        @RequestParam(required = false) MultipartFile imagen
    ) {
        CrearHistoriaCommand command = new CrearHistoriaCommand(
            usuarioId, texto, colorFondo, duracion, imagen
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        // TODO: EliminarHistoriaUseCase cuando lo implementemos
        return ResponseEntity.noContent().build();
    }
}