package com.comunired.quejas.infrastructure.adapter.in.rest;

import com.comunired.quejas.application.dto.in.QuejaCommands.*;
import com.comunired.quejas.application.dto.out.QuejaResponses.*;
import com.comunired.quejas.application.port.in.QuejaPorts.*;
import com.comunired.quejas.application.port.out.QuejaOutPorts.QuejaImagenPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller REST del módulo quejas.
 *
 * Responsabilidades:
 *  - Recibir multipart (imagen) y subirla a Cloudinary antes de delegar al use case
 *  - Operaciones simples: PATCH estado, PATCH riesgo, DELETE
 *
 * Las queries con joins (feed, detalle) van por GraphQL.
 */
@RestController
@RequestMapping("/api/quejas")
public class QuejaRestController {

    private final CrearQuejaUseCase crearQueja;
    private final ActualizarQuejaUseCase actualizarQueja;
    private final ClasificarRiesgoUseCase clasificarRiesgo;
    private final CambiarEstadoQuejaUseCase cambiarEstado;
    private final EliminarQuejaUseCase eliminarQueja;
    private final QuejaImagenPort imagenPort;

    public QuejaRestController(CrearQuejaUseCase crearQueja,
                                ActualizarQuejaUseCase actualizarQueja,
                                ClasificarRiesgoUseCase clasificarRiesgo,
                                CambiarEstadoQuejaUseCase cambiarEstado,
                                EliminarQuejaUseCase eliminarQueja,
                                QuejaImagenPort imagenPort) {
        this.crearQueja = crearQueja;
        this.actualizarQueja = actualizarQueja;
        this.clasificarRiesgo = clasificarRiesgo;
        this.cambiarEstado = cambiarEstado;
        this.eliminarQueja = eliminarQueja;
        this.imagenPort = imagenPort;
    }

    // -------------------------------------------------------------------------
    // POST /api/quejas — ciudadano crea una queja con imagen opcional
    // -------------------------------------------------------------------------
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuejaResponse> crear(
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String categoriaId,
            @RequestParam String ubicacion,
            @RequestParam String usuarioId,           // TODO: reemplazar por @AuthenticationPrincipal
            @RequestParam(required = false) MultipartFile imagen
    ) {
        String imagenUrl = (imagen != null && !imagen.isEmpty())
                ? imagenPort.subirImagen(imagen)
                : null;

        QuejaResponse response = crearQueja.ejecutar(new CrearQuejaCommand(
                titulo, descripcion, categoriaId, ubicacion, usuarioId, imagenUrl
        ));

        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------------------------------
    // PATCH /api/quejas/{id} — editar datos básicos (sin imagen)
    // -------------------------------------------------------------------------
    @PatchMapping("/{id}")
    public ResponseEntity<QuejaResponse> actualizar(
            @PathVariable String id,
            @RequestBody ActualizarQuejaBody body
    ) {
        QuejaResponse response = actualizarQueja.ejecutar(new ActualizarQuejaCommand(
                id, body.titulo(), body.descripcion(), body.categoriaId(), body.ubicacion(), body.imagenUrl()
        ));
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------------------------------
    // PATCH /api/quejas/{id}/riesgo — soporte clasifica nivel de riesgo
    // -------------------------------------------------------------------------
    @PatchMapping("/{id}/riesgo")
    public ResponseEntity<QuejaResponse> clasificar(
            @PathVariable String id,
            @RequestBody ClasificarRiesgoBody body
    ) {
        QuejaResponse response = clasificarRiesgo.ejecutar(new ClasificarRiesgoCommand(
                id, body.soporteId(), body.nivelRiesgo(), body.observacion()
        ));
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------------------------------
    // PATCH /api/quejas/{id}/estado — municipio/admin cambia estado
    // -------------------------------------------------------------------------
    @PatchMapping("/{id}/estado")
    public ResponseEntity<QuejaResponse> cambiarEstado(
            @PathVariable String id,
            @RequestBody CambiarEstadoBody body
    ) {
        QuejaResponse response = cambiarEstado.ejecutar(new CambiarEstadoCommand(
                id, body.usuarioId(), body.nuevoEstadoClave(), body.observacion()
        ));
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------------------------------
    // DELETE /api/quejas/{id}
    // -------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(
            @PathVariable String id,
            @RequestParam String usuarioId    // TODO: @AuthenticationPrincipal
    ) {
        boolean eliminada = eliminarQueja.ejecutar(new EliminarQuejaCommand(id, usuarioId));
        return ResponseEntity.ok(eliminada);
    }

    // -------------------------------------------------------------------------
    // Request bodies (records inline — sin clases separadas para cosas simples)
    // -------------------------------------------------------------------------
    record ActualizarQuejaBody(String titulo, String descripcion,
                               String categoriaId, String ubicacion, String imagenUrl) {}

    record ClasificarRiesgoBody(String soporteId, String nivelRiesgo, String observacion) {}

    record CambiarEstadoBody(String usuarioId, String nuevoEstadoClave, String observacion) {}
}
