package com.comunired.usuarios.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;

@Service
public class UsuariosService {
    private final UsuariosRepository usuariosRepository;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    // Paginado
    public Page<Usuario> obtenerUsuarios(int page, int size) {
        return usuariosRepository.findAll(PageRequest.of(page, size));
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuariosRepository.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuariosRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuariosRepository.findByEmail(email);
    }

    public Usuario buscarPorId(String id) {
        return usuariosRepository.findById(id);
    }

    public void eliminarUsuario(String id) {
        usuariosRepository.deleteById(id);
    }

    // Conteo por rol
    public long contarPorRol(String rol_id) {
        return usuariosRepository.countByRolId(rol_id);
    }

    // Login (solo verifica email y password)
    public Usuario login(String email, String password) {
        Usuario usuario = usuariosRepository.findByEmail(email);
        if (usuario != null && usuario.getPassword().equals(password)) { // En producción, usar BCrypt
            return usuario;
        }
        return null;
    }
}