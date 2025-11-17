package com.comunired.seguimientos.graphql.resolver;

import com.comunired.seguimientos.application.dto.EstadoRelacionDTO;

import com.comunired.seguimientos.application.dto.SeguimientoDTO;
import com.comunired.seguimientos.application.service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SeguimientoResolver {
    
    @Autowired
    private SeguimientoService seguimientoService;

    
    @QueryMapping
    public Map<String, Object> seguidoresDe(@Argument String usuarioId,
                                             @Argument int page,
                                             @Argument int size) {
        Page<SeguimientoDTO> resultado = seguimientoService.obtenerSeguidores(usuarioId, page, size);
        return createPageResponse(resultado);
    }

    @QueryMapping
    public Map<String, Object> seguidosPor(@Argument String usuarioId,
                                            @Argument int page,
                                            @Argument int size) {
        Page<SeguimientoDTO> resultado = seguimientoService.obtenerSeguidos(usuarioId, page, size);
        return createPageResponse(resultado);
    }

    @QueryMapping
    public Map<String, Object> solicitudesPendientes(@Argument String usuarioId,
                                                      @Argument int page,
                                                      @Argument int size) {
        Page<SeguimientoDTO> resultado = seguimientoService.obtenerSolicitudesPendientes(usuarioId, page, size);
        return createPageResponse(resultado);
    }

    @QueryMapping
    public Map<String, Object> solicitudesEnviadas(@Argument String usuarioId,
                                                    @Argument int page,
                                                    @Argument int size) {
        Page<SeguimientoDTO> resultado = seguimientoService.obtenerSolicitudesEnviadas(usuarioId, page, size);
        return createPageResponse(resultado);
    }

    @QueryMapping
    public Boolean estaSiguiendo(@Argument String seguidorId,
                                  @Argument String seguidoId) {
        return seguimientoService.estaSiguiendo(seguidorId, seguidoId);
    }

    @QueryMapping
    public Boolean seSiguenMutuamente(@Argument String usuarioId1,
                                       @Argument String usuarioId2) {
        return seguimientoService.seSiguenMutuamente(usuarioId1, usuarioId2);
    }

    @QueryMapping
    public Integer contarSeguidores(@Argument String usuarioId) {
        return (int) seguimientoService.contarSeguidores(usuarioId);
    }

    @QueryMapping
    public Integer contarSeguidos(@Argument String usuarioId) {
        return (int) seguimientoService.contarSeguidos(usuarioId);
    }

    
    @MutationMapping
    public SeguimientoDTO enviarSolicitudSeguimiento(@Argument String seguidorId,
                                                      @Argument String seguidoId) {
        return seguimientoService.enviarSolicitud(seguidorId, seguidoId);
    }

    @MutationMapping
    public SeguimientoDTO aceptarSolicitud(@Argument String seguimientoId) {
        return seguimientoService.aceptarSolicitud(seguimientoId);
    }

    @MutationMapping
    public Boolean rechazarSolicitud(@Argument String seguimientoId) {
        return seguimientoService.rechazarSolicitud(seguimientoId);
    }

    @MutationMapping
    public Boolean cancelarSolicitud(@Argument String seguimientoId) {
        return seguimientoService.cancelarSolicitud(seguimientoId);
    }

    @MutationMapping
    public Boolean dejarDeSeguir(@Argument String seguidorId,
                                  @Argument String seguidoId) {
        return seguimientoService.dejarDeSeguir(seguidorId, seguidoId);
    }


    @QueryMapping
    public EstadoRelacionDTO estadoSeguimiento(@Argument String usuarioActualId,
                                                @Argument String otroUsuarioId) {
        return seguimientoService.obtenerEstadoRelacion(usuarioActualId, otroUsuarioId);
    }

    
    private Map<String, Object> createPageResponse(Page<SeguimientoDTO> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("number", page.getNumber());
        response.put("size", page.getSize());
        return response;
    }
}
