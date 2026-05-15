package com.comunired.reels.infrastructure.adapter.in.rest;

import com.comunired.reels.application.dto.in.ActualizarReelCommand;
import com.comunired.reels.application.dto.in.CrearReelCommand;
import com.comunired.reels.application.dto.out.ReelResponse;
import com.comunired.reels.application.port.in.GestionarReelUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reels")
public class AdminReelController {

    private final GestionarReelUseCase gestionarReelUseCase;

    public AdminReelController(GestionarReelUseCase gestionarReelUseCase) {
        this.gestionarReelUseCase = gestionarReelUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ReelResponse>> listarTodos(
            @RequestParam(required = false, defaultValue = "") String search) {
        return ResponseEntity.ok(gestionarReelUseCase.listarTodos(search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReelResponse> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(gestionarReelUseCase.obtenerPorId(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReelResponse> crear(
            @RequestParam MultipartFile video,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String authorId,
            @RequestParam String author,
            @RequestParam(required = false) String avatarUrl) {
        CrearReelCommand command = new CrearReelCommand(
            video, title, description, authorId, author, avatarUrl);
        return ResponseEntity.ok(gestionarReelUseCase.crear(command));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReelResponse> actualizar(
            @PathVariable String id,
            @RequestParam(required = false) MultipartFile video,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String author,
            @RequestParam(required = false) String avatarUrl) {
        ActualizarReelCommand command = new ActualizarReelCommand(
            video, title, description, author, avatarUrl);
        return ResponseEntity.ok(gestionarReelUseCase.actualizar(id, command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        gestionarReelUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
