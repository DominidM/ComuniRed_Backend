package com.comunired.asignaciones.graphql.resolver;

import com.comunired.asignaciones.application.service.AsignacionesService;
import com.comunired.asignaciones.application.dto.AsignacionesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class AsignacionesResolver {

    @Autowired
    private AsignacionesService asignacionesService;

    // ========== QUERIES ==========
    
    @QueryMapping
    public AsignacionesDTO asignacionPorId(@Argument String id) {
        return asignacionesService.findById(id);
    }

    @QueryMapping
    public List<AsignacionesDTO> asignacionesPorQueja(@Argument String quejaId) {
        return asignacionesService.findAllByQuejaId(quejaId);
    }

    @QueryMapping
    public List<AsignacionesDTO> asignacionesPorSoporte(@Argument String soporteId) {
        return asignacionesService.findBySoporteId(soporteId);
    }

    @QueryMapping
    public List<AsignacionesDTO> asignacionesActivas() {
        return asignacionesService.findActivas();
    }

    // ========== MUTATIONS ==========
    
    @MutationMapping
    public AsignacionesDTO asignarQueja(@Argument String quejaId,
                                       @Argument String soporteId,
                                       @Argument String adminId,
                                       @Argument String observacion) {
        return asignacionesService.asignar(quejaId, soporteId, adminId, observacion);
    }

    @MutationMapping
    public AsignacionesDTO cambiarEstadoAsignacion(@Argument String asignacionId,
                                                   @Argument String nuevoEstado,
                                                   @Argument String soporteId) {
        return asignacionesService.cambiarEstado(asignacionId, nuevoEstado, soporteId);
    }

    @MutationMapping
    public AsignacionesDTO reasignarQueja(@Argument String asignacionId,
                                         @Argument String nuevoSoporteId,
                                         @Argument String adminId,
                                         @Argument String motivo) {
        return asignacionesService.reasignar(asignacionId, nuevoSoporteId, adminId, motivo);
    }
}
