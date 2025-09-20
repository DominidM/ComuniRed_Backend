package com.comunired.usuarios.graphql.resolver;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.comunired.usuarios.application.service.UsuariosService;
import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.application.dto.UsuariosDTO;

@Controller
public class UsuariosResolver {

    private final UsuariosService usuariosService;

    public UsuariosResolver(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    // Mapeo de Usuario a UsuariosDTO
    private UsuariosDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;
        UsuariosDTO dto = new UsuariosDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setDni(usuario.getDni());
        dto.setNumero_telefono(usuario.getNumero_telefono());
        dto.setEdad(usuario.getEdad());
        dto.setSexo(usuario.getSexo());
        dto.setDistrito(usuario.getDistrito());
        dto.setCodigo_postal(usuario.getCodigo_postal());
        dto.setDireccion(usuario.getDireccion());
        dto.setEmail(usuario.getEmail());
        dto.setRol_id(usuario.getRol_id());
        return dto;
    }

    @QueryMapping
    public Page<UsuariosDTO> obtenerUsuarios(@Argument int page, @Argument int size) {
        Page<Usuario> usuarios = usuariosService.obtenerUsuarios(page, size);
        List<UsuariosDTO> dtos = usuarios.getContent().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return new PageImpl<>(dtos, usuarios.getPageable(), usuarios.getTotalElements());
    }

    @QueryMapping
    public List<UsuariosDTO> obtenerTodosLosUsuarios() {
        return usuariosService.obtenerTodosLosUsuarios().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @QueryMapping
    public long contarUsuariosPorRol(@Argument String rol_id) {
        return usuariosService.contarPorRol(rol_id);
    }

    @QueryMapping
    public UsuariosDTO obtenerUsuarioPorId(@Argument String id) {
        return toDTO(usuariosService.buscarPorId(id));
    }

    @MutationMapping
    public UsuariosDTO crearUsuario(@Argument Usuario usuario) {
        return toDTO(usuariosService.guardarUsuario(usuario));
    }

    @MutationMapping
    public UsuariosDTO actualizarUsuario(@Argument String id, @Argument Usuario usuario) {
        Usuario existente = usuariosService.buscarPorId(id);
        if (existente != null) {
            usuario.setId(id);
            return toDTO(usuariosService.guardarUsuario(usuario));
        }
        return null;
    }

    @MutationMapping
    public Boolean eliminarUsuario(@Argument String id) {
        usuariosService.eliminarUsuario(id);
        return true;
    }

    @MutationMapping
    public UsuariosDTO login(@Argument String email, @Argument String password) {
        return toDTO(usuariosService.login(email, password));
    }
}