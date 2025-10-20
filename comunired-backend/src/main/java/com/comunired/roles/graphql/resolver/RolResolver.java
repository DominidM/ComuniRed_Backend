package com.comunired.roles.graphql.resolver;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.comunired.roles.application.service.RolService;
import com.comunired.roles.domain.entity.Rol;

@Controller
public class RolResolver {

    private static final Logger logger = LoggerFactory.getLogger(RolResolver.class);

    private final RolService rolService;

    public RolResolver(RolService rolService) {
        this.rolService = rolService;
    }

    @QueryMapping
    public Page<Rol> obtenerRoles(@Argument int page, @Argument int size) {
        return rolService.obtenerRoles(page, size);
    }

    /**
     * Devuelve todos los roles. IMPORTANTE: nunca debe devolver null.
     * Si el servicio retorna null o lanza excepción se devuelve Collections.emptyList()
     * para evitar que GraphQL lance errores por tipos non-null.
     */
    @QueryMapping
    public List<Rol> obtenerTodosLosRoles() {
        try {
            List<Rol> roles = rolService.obtenerTodosLosRoles();
            if (roles == null) {
                logger.warn("RolService.obtenerTodosLosRoles() devolvió null — devolviendo lista vacía en su lugar.");
                return Collections.emptyList();
            }
            return roles;
        } catch (Exception e) {
            logger.error("Error en obtenerTodosLosRoles(), devolviendo lista vacía", e);
            return Collections.emptyList();
        }
    }

    /**
     * Query para obtener un rol por id (útil como fallback desde el frontend).
     */
    @QueryMapping
    public Rol obtenerRolPorId(@Argument String id) {
        try {
            if (id == null || id.trim().isEmpty()) return null;
            return rolService.buscarPorId(id);
        } catch (Exception e) {
            logger.error("Error al obtener rol por id=" + id, e);
            return null;
        }
    }

    @MutationMapping
    public Rol crearRol(@Argument String nombre, @Argument String descripcion) {
        Rol rol = new Rol();
        rol.setNombre(nombre);
        rol.setDescripcion(descripcion);
        return rolService.guardarRol(rol);
    }

    @MutationMapping
    public Rol editarRol(@Argument String id, @Argument String nombre, @Argument String descripcion) {
        Rol rol = rolService.buscarPorId(id);
        if (rol == null) throw new RuntimeException("Rol no existe");
        rol.setNombre(nombre);
        rol.setDescripcion(descripcion);
        return rolService.guardarRol(rol);
    }

    @MutationMapping
    public boolean eliminarRol(@Argument String id) {
        try {
            boolean eliminado = rolService.eliminarPorId(id);
            logger.info("Intentando eliminar rol con id: {}. Resultado: {}", id, eliminado);
            return eliminado;
        } catch (Exception e) {
            logger.error("Error eliminando rol con id: " + id, e);
            return false;
        }
    }
}