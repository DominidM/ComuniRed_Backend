package com.comunired.roles.graphql.resolver;

import java.util.List;

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
    public List<Rol> obtenerRoles() {
        return rolService.obtenerRoles();
    }
}