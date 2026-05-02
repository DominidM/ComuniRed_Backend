package com.comunired.quejas.infrastructure.adapter.in.graphql;

import com.comunired.quejas.application.dto.out.QuejaResponses.*;
import com.comunired.quejas.application.port.in.QuejaPorts.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Resolver GraphQL del módulo quejas.
 *
 * Solo queries — lecturas con joins entre dominios.
 * Las mutations (crear, actualizar, eliminar) van por REST.
 */
@Controller
public class QuejaGraphQLResolver {

    private final ObtenerQuejaUseCase obtenerQueja;
    private final ListarQuejasUseCase listarQuejas;
    private final ListarQuejasPaginadasUseCase listarPaginadas;
    private final ListarQuejasPorUsuarioUseCase listarPorUsuario;
    private final ListarQuejasAprobadasUseCase listarAprobadas;
    private final ListarQuejasParaRevisarUseCase listarParaRevisar;

    public QuejaGraphQLResolver(ObtenerQuejaUseCase obtenerQueja,
                                 ListarQuejasUseCase listarQuejas,
                                 ListarQuejasPaginadasUseCase listarPaginadas,
                                 ListarQuejasPorUsuarioUseCase listarPorUsuario,
                                 ListarQuejasAprobadasUseCase listarAprobadas,
                                 ListarQuejasParaRevisarUseCase listarParaRevisar) {
        this.obtenerQueja = obtenerQueja;
        this.listarQuejas = listarQuejas;
        this.listarPaginadas = listarPaginadas;
        this.listarPorUsuario = listarPorUsuario;
        this.listarAprobadas = listarAprobadas;
        this.listarParaRevisar = listarParaRevisar;
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
    public List<QuejaResponse> quejasPorUsuario(@Argument String usuarioId,
                                                 @Argument String usuarioActualId) {
        return listarPorUsuario.ejecutar(usuarioId, usuarioActualId);
    }

    @QueryMapping
    public List<QuejaResponse> quejasAprobadas(@Argument String usuarioActualId) {
        return listarAprobadas.ejecutar(usuarioActualId);
    }

    @QueryMapping
    public List<QuejaResponse> quejasParaRevisar(@Argument String usuarioActualId) {
        return listarParaRevisar.ejecutar(usuarioActualId);
    }
}
