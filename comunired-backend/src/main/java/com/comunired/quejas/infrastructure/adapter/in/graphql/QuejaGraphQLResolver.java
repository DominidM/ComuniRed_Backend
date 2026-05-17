package com.comunired.quejas.infrastructure.adapter.in.graphql;

import com.comunired.quejas.application.dto.in.QuejaCommands.ActualizarQuejaCommand;
import com.comunired.quejas.application.dto.in.QuejaCommands.CrearQuejaCommand;
import com.comunired.quejas.application.dto.out.QuejaResponses.*;
import com.comunired.quejas.application.port.in.QuejaPorts.*;
import com.comunired.quejas.application.port.out.QuejaOutPorts.QuejaImagenPort;
import com.comunired.reacciones.application.service.ReaccionesService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
@Controller
public class QuejaGraphQLResolver {

    private final ObtenerQuejaUseCase obtenerQueja;
    private final ListarQuejasUseCase listarQuejas;
    private final ListarQuejasPaginadasUseCase listarPaginadas;
    private final ListarQuejasPorUsuarioUseCase listarPorUsuario;
    private final ListarQuejasAprobadasUseCase listarAprobadas;
    private final ListarQuejasParaRevisarUseCase listarParaRevisar;
    private final ListarQuejasAdminPaginadasUseCase listarAdminPaginadas;

    private final CrearQuejaUseCase crearQueja;
    private final ActualizarQuejaUseCase actualizarQueja;
    private final QuejaImagenPort imagenPort;
    private final ReaccionesService reaccionesService;

    public QuejaGraphQLResolver(ObtenerQuejaUseCase obtenerQueja,
            ListarQuejasUseCase listarQuejas,
            ListarQuejasPaginadasUseCase listarPaginadas,
            ListarQuejasPorUsuarioUseCase listarPorUsuario,
            ListarQuejasAprobadasUseCase listarAprobadas,
            ListarQuejasParaRevisarUseCase listarParaRevisar,
            ListarQuejasAdminPaginadasUseCase listarAdminPaginadas,
            CrearQuejaUseCase crearQueja,
            ActualizarQuejaUseCase actualizarQueja,
            QuejaImagenPort imagenPort,
            ReaccionesService reaccionesService) {
        this.obtenerQueja = obtenerQueja;
        this.listarQuejas = listarQuejas;
        this.listarPaginadas = listarPaginadas;
        this.listarPorUsuario = listarPorUsuario;
        this.listarAprobadas = listarAprobadas;
        this.listarParaRevisar = listarParaRevisar;
        this.listarAdminPaginadas = listarAdminPaginadas;
        this.crearQueja = crearQueja;
        this.actualizarQueja = actualizarQueja;
        this.imagenPort = imagenPort;
        this.reaccionesService = reaccionesService;
    }
    @QueryMapping
    public QuejaResponse obtenerQuejaPorId(@Argument String id,
            @Argument String usuarioActualId) {
        return obtenerQueja.ejecutar(id, usuarioActualId);
    }

    @QueryMapping
    public List<QuejaResponse> obtenerQuejas(@Argument String usuarioActualId) {
        return listarQuejas.ejecutar(usuarioActualId);
    }

    @QueryMapping
    public QuejaPageResponse obtenerQuejasPaginadas(@Argument String usuarioActualId,
            @Argument int page,
            @Argument int size) {
        return listarPaginadas.ejecutar(usuarioActualId, page, size);
    }

    @QueryMapping
    public QuejaPageResponse obtenerQuejasAdminPaginadas(@Argument String usuarioActualId,
            @Argument int page,
            @Argument int size) {
        return listarAdminPaginadas.ejecutar(usuarioActualId, page, size);
    }

    @QueryMapping
    public List<QuejaResponse> quejasPorUsuario(@Argument String usuarioId,
            @Argument String usuarioActualId) {
        return listarPorUsuario.ejecutar(usuarioId, usuarioActualId);
    }

    @QueryMapping
    public List<QuejaResponse> quejasAprobadas(@Argument String usuarioActualId) {
        return listarAprobadas.ejecutar(usuarioActualId);
    }

    @MutationMapping
    public QuejaResponse crearQueja(
            @Argument String titulo,
            @Argument String descripcion,
            @Argument String categoriaId,
            @Argument String ubicacion,
            @Argument Double lat,
            @Argument Double lng,
            @Argument String usuarioId
    ) {
        return crearQueja.ejecutar(new CrearQuejaCommand(
                titulo, descripcion, categoriaId, ubicacion, usuarioId, null, lat, lng
        ));
    }

    @MutationMapping
    public QuejaResponse actualizarQueja(
            @Argument String id,
            @Argument String titulo,
            @Argument String descripcion,
            @Argument String categoriaId,
            @Argument String ubicacion,
            @Argument("imagen_url") String imagenUrl
    ) {
        return actualizarQueja.ejecutar(new ActualizarQuejaCommand(
                id, titulo, descripcion, categoriaId, ubicacion, imagenUrl
        ));
    }

    @MutationMapping
    public QuejaResponse votarQueja(
            @Argument String quejaId,
            @Argument String usuarioId,
            @Argument String voto
    ) {
        String tipoReaccion = switch (voto.toUpperCase()) {
            case "SI", "YES", "ACCEPT" -> "accept";
            case "NO", "REJECT" -> "reject";
            default -> throw new IllegalArgumentException("Voto inválido: " + voto);
        };
        reaccionesService.toggleReaction(quejaId, tipoReaccion, usuarioId);
        return obtenerQueja.ejecutar(quejaId, usuarioId);
    }

    @QueryMapping
    public List<QuejaResponse> quejasParaRevisar(@Argument String usuarioActualId) {
        return listarParaRevisar.ejecutar(usuarioActualId);
    }
}
