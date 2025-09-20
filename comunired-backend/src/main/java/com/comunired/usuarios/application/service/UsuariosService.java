package com.comunired.usuarios.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;

@Service
public class UsuariosService {
    private final UsuariosRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Paginado
    public Page<Usuario> obtenerUsuarios(int page, int size) {
        return usuariosRepository.findAll(PageRequest.of(page, size));
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuariosRepository.findAll();
    }

    // Guarda usuario hasheando la contraseña si es nueva
    public Usuario guardarUsuario(Usuario usuario) {
        // Solo hashea si la contraseña no está hasheada aún (opcional, puedes mejorar la validación)
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            String hashedPassword = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(hashedPassword);
        }
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

    public Usuario login(String email, String password) {
        Usuario usuario = usuariosRepository.findByEmail(email);
        System.out.println("Email: " + email);
        System.out.println("Password recibido (plain): " + password);
        System.out.println("Password hash en BD: " + (usuario != null ? usuario.getPassword() : "null"));
        boolean match = usuario != null && passwordEncoder.matches(password, usuario.getPassword());
        System.out.println("Password match: " + match);
        if (match) {
            return usuario;
        }
        return null;
    }
}