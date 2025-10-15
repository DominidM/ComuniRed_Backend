package com.comunired.roles.graphql.resolver;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.comunired.roles.application.service.RolService;
import com.comunired.roles.domain.entity.Rol;

@Controller
public class RolResolver {

    private final RolService rolService;

    public RolResolver(RolService rolService) {
        this.rolService = rolService;
    }

    @QueryMapping
    public Page<Rol> obtenerRoles(@Argument int page, @Argument int size) {
        return rolService.obtenerRoles(page, size);
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
            System.out.println("Intentando eliminar rol con id: " + id + ". Resultado: " + eliminado);
            return eliminado;
        } catch (Exception e) {
            System.err.println("Error eliminando rol con id: " + id + ". " + e.getMessage());
            return false;
        }
    }
}