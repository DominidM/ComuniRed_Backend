package com.comunired.estados_queja.graphql.resolver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.comunired.estados_queja.application.dto.Estados_quejaDTO;
import com.comunired.estados_queja.application.service.Estados_quejaService;
import com.comunired.estados_queja.domain.entity.Estados_queja;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Controller
public class Estados_quejaResolver {

    private static final Logger logger = LoggerFactory.getLogger(Estados_quejaResolver.class);

    private final Estados_quejaService estadosQuejaService;

    public Estados_quejaResolver(Estados_quejaService estadosQuejaService) {
        this.estadosQuejaService = estadosQuejaService;
    }

    private Estados_quejaDTO toDTO(Estados_queja e) {
        if (e == null) return null;
        Estados_quejaDTO dto = new Estados_quejaDTO();
        dto.setId(e.getId());
        dto.setClave(e.getClave());
        dto.setNombre(e.getNombre());
        dto.setDescripcion(e.getDescripcion());
        dto.setOrden(e.getOrden());
        return dto;
    }

    @QueryMapping(name = "listarEstados")
    public List<Estados_quejaDTO> listarEstados() {
        List<Estados_queja> lista = estadosQuejaService.listarEstados();
        return lista.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public Estados_quejaDTO obtenerEstadoPorId(@Argument String id) {
        Optional<Estados_queja> opt = estadosQuejaService.listarEstados()
                .stream()
                .filter(e -> e != null && id != null && id.equals(e.getId()))
                .findFirst();
        return opt.map(this::toDTO).orElse(null);
    }

    @QueryMapping(name = "buscarEstadoPorNombre")
    public Estados_quejaDTO buscarEstadoPorNombre(@Argument String nombre) {
        Optional<Estados_queja> opt = estadosQuejaService.buscarPorNombre(nombre);
        return opt.map(this::toDTO).orElse(null);
    }


    @MutationMapping
    public Estados_quejaDTO crearEstado(@Argument String clave, @Argument String nombre,
                                        @Argument String descripcion, @Argument int orden) {
        try {
            Estados_queja e = estadosQuejaService.crearEstado(clave, nombre, descripcion, orden);
            return toDTO(e);
        } catch (Exception ex) {
            logger.error("Error creando estado de queja", ex);
            return null;
        }
    }

    @MutationMapping
    public Estados_quejaDTO actualizarEstado(@Argument String id, @Argument String clave,
                                             @Argument String nombre, @Argument String descripcion,
                                             @Argument int orden) {
        try {
            Estados_queja e = estadosQuejaService.actualizarEstado(id, clave, nombre, descripcion, orden);
            return toDTO(e);
        } catch (Exception ex) {
            logger.error("Error actualizando estado id={}", id, ex);
            return null;
        }
    }

    @MutationMapping
    public boolean eliminarEstado(@Argument String id) {
        try {
            estadosQuejaService.eliminarEstado(id);
            return true;
        } catch (Exception ex) {
            logger.error("Error eliminando estado id={}", id, ex);
            return false;
        }
    }

    @QueryMapping(name = "obtenerEstados_queja")
    public Page<Estados_quejaDTO> obtenerEstadosQueja(@Argument int page, @Argument int size) {
        Page<Estados_queja> pageResult = estadosQuejaService.listarEstadosQuejaPaginado(page, size);
        List<Estados_quejaDTO> dtos = pageResult.getContent().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return new org.springframework.data.domain.PageImpl<>(dtos, pageResult.getPageable(), pageResult.getTotalElements());
    }

    @QueryMapping(name = "contarEstadosQueja")
    public int contarEstadosQueja() {
        return (int) estadosQuejaService.contarTotalEstados();
    }
}
