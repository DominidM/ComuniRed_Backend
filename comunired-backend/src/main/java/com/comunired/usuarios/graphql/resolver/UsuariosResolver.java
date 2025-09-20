package com.comunired.usuarios.graphql.resolver;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.comunired.usuarios.application.service.UsuariosService;
import com.comunired.usuarios.domain.entity.Usuario;

@Controller
public class UsuariosResolver {

    private final UsuariosService usuariosService;

    public UsuariosResolver(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @QueryMapping
    public Page<Usuario> obtenerUsuarios(@Argument int page, @Argument int size) {
        return usuariosService.obtenerUsuarios(page, size);
    }

    @QueryMapping
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuariosService.obtenerTodosLosUsuarios();
    }

    @QueryMapping
    public long contarUsuariosPorRol(@Argument String rol_id) {
        return usuariosService.contarPorRol(rol_id);
    }

    @QueryMapping
    public Usuario obtenerUsuarioPorId(@Argument String id) {
        return usuariosService.buscarPorId(id);
    }

    @MutationMapping
    public Usuario crearUsuario(@Argument Usuario usuario) {
        return usuariosService.guardarUsuario(usuario);
    }

    @MutationMapping
    public Usuario actualizarUsuario(@Argument String id, @Argument Usuario usuario) {
        Usuario existente = usuariosService.buscarPorId(id);
        if (existente != null) {
            usuario.setId(id);
            return usuariosService.guardarUsuario(usuario);
        }
        return null;
    }

    @MutationMapping
    public Boolean eliminarUsuario(@Argument String id) {
        usuariosService.eliminarUsuario(id);
        return true;
    }

    @MutationMapping
    public Usuario login(@Argument String email, @Argument String password) {
        return usuariosService.login(email, password);
    }
}