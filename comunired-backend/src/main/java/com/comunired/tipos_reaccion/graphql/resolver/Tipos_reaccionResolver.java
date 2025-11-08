package com.comunired.tipos_reaccion.graphql.resolver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.comunired.tipos_reaccion.application.dto.Tipos_reaccionDTO;
import com.comunired.tipos_reaccion.application.service.Tipos_reaccionService;
import com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Controller
public class Tipos_reaccionResolver {

    private static final Logger logger = LoggerFactory.getLogger(Tipos_reaccionResolver.class);

    private final Tipos_reaccionService tiposReaccionService;

    public Tipos_reaccionResolver(Tipos_reaccionService tiposReaccionService) {
        this.tiposReaccionService = tiposReaccionService;
    }

    private Tipos_reaccionDTO toDTO(Tipos_reaccion t) {
        if (t == null) return null;
        Tipos_reaccionDTO dto = new Tipos_reaccionDTO();
        dto.setId(t.getId());
        dto.setKey(t.getKey());
        dto.setLabel(t.getLabel());
        dto.setActivo(t.isActivo());
        dto.setOrden(t.getOrden());
        return dto;
    }

    // 🔹 Listar todos los tipos de reacción
    @QueryMapping(name = "listarTiposReaccion")
    public List<Tipos_reaccionDTO> listarTiposReaccion() {
        return tiposReaccionService.listarTiposReaccion()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping(name = "obtenerTipoReaccionPorId")
    public Tipos_reaccionDTO obtenerTipoReaccionPorId(@Argument String id) {
        Optional<Tipos_reaccion> opt = tiposReaccionService.buscarPorId(id);
        return opt.map(this::toDTO).orElse(null);
    }

    @QueryMapping(name = "buscarTipoReaccionPorLabel")
    public Tipos_reaccionDTO buscarTipoReaccionPorLabel(@Argument String label) {
        Optional<Tipos_reaccion> opt = tiposReaccionService.buscarPorLabel(label);
        return opt.map(this::toDTO).orElse(null);
    }

    @MutationMapping
    public Tipos_reaccionDTO crearTipoReaccion(@Argument String key, @Argument String label, @Argument Boolean activo, @Argument Integer orden) {
        try {
            Tipos_reaccion t = tiposReaccionService.crearTipoReaccion(key, label, activo, orden);
            return toDTO(t);
        } catch (Exception e) {
            logger.error("Error al crear tipo de reacción", e);
            return null;
        }
    }

    @MutationMapping
    public Tipos_reaccionDTO actualizarTipoReaccion(@Argument String id, @Argument String key, @Argument String label, @Argument Boolean activo, @Argument Integer orden) {
        try {
            Tipos_reaccion t = tiposReaccionService.actualizarTipoReaccion(id, key, label, activo, orden);
            return toDTO(t);
        } catch (Exception e) {
            logger.error("Error al actualizar tipo de reacción id={}", id, e);
            return null;
        }
    }

    @MutationMapping
    public boolean eliminarTipoReaccion(@Argument String id) {
        try {
            tiposReaccionService.eliminarTipoReaccion(id);
            return true;
        } catch (Exception e) {
            logger.error("Error al eliminar tipo de reacción id={}", id, e);
            return false;
        }
    }

    @QueryMapping(name = "obtenerTipo_reaccion")
    public Page<Tipos_reaccionDTO> obtenerTiposReaccion(@Argument int page, @Argument int size) {
        Page<Tipos_reaccion> pageResult = tiposReaccionService.listarTiposReaccionPaginado(page, size);
        List<Tipos_reaccionDTO> dtos = pageResult.getContent().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return new org.springframework.data.domain.PageImpl<>(dtos, pageResult.getPageable(), pageResult.getTotalElements());
    }
}
