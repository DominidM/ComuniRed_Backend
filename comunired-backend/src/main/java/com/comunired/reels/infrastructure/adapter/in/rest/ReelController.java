package com.comunired.reels.infrastructure.adapter.in.rest;

import com.comunired.reels.application.dto.out.ReelComentarioResponse;
import com.comunired.reels.application.dto.out.ReelResponse;
import com.comunired.reels.application.port.in.ObtenerReelsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reels")
public class ReelController {

    private final ObtenerReelsUseCase obtenerReelsUseCase;

    public ReelController(ObtenerReelsUseCase obtenerReelsUseCase) {
        this.obtenerReelsUseCase = obtenerReelsUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ReelResponse>> obtenerActivos(
            @RequestParam String usuarioId) {
        return ResponseEntity.ok(obtenerReelsUseCase.obtenerActivos(usuarioId));
    }

    @PatchMapping("/{id}/like")
    public ResponseEntity<ReelResponse> marcarLike(
            @PathVariable String id,
            @RequestParam String usuarioId) {
        return ResponseEntity.ok(obtenerReelsUseCase.marcarLike(id, usuarioId));
    }

    @PatchMapping("/{id}/save")
    public ResponseEntity<ReelResponse> marcarSave(
            @PathVariable String id,
            @RequestParam String usuarioId) {
        return ResponseEntity.ok(obtenerReelsUseCase.marcarSave(id, usuarioId));
    }

    @PatchMapping("/{id}/view")
    public ResponseEntity<ReelResponse> incrementarVista(@PathVariable String id) {
        return ResponseEntity.ok(obtenerReelsUseCase.incrementarVista(id));
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ReelComentarioResponse> comentar(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String usuarioId = body.get("usuarioId");
        String usuarioNombre = body.get("usuarioNombre");
        String usuarioAvatar = body.get("usuarioAvatar");
        String texto = body.get("texto");
        return ResponseEntity.ok(obtenerReelsUseCase.comentar(id, usuarioId, usuarioNombre, usuarioAvatar, texto));
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<ReelComentarioResponse>> obtenerComentarios(@PathVariable String id) {
        return ResponseEntity.ok(obtenerReelsUseCase.obtenerComentarios(id));
    }
}
