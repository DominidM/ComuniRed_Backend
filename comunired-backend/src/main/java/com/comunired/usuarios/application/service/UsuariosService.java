package com.comunired.usuarios.application.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;

import java.time.Instant;

@Service
public class UsuariosService {
    private static final Logger logger = LoggerFactory.getLogger(UsuariosService.class);
    private final UsuariosRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario guardarUsuario(Usuario usuario) {
        logger.info("[guardarUsuario] Intentando guardar usuario: email={}, nombre={}, fecha_nacimiento={}, fecha_registro={}",
            usuario.getEmail(), usuario.getNombre(), usuario.getFecha_nacimiento(), usuario.getFecha_registro());

        // ❌ REMOVER: No calcular edad porque el campo no existe
        // La edad se puede calcular en el frontend si es necesario

        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            String hashedPassword = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(hashedPassword);
            logger.info("[guardarUsuario] Password hasheada para email={}", usuario.getEmail());
        }

        if (usuario.getFecha_registro() == null) {
            usuario.setFecha_registro(Instant.now());
            logger.info("[guardarUsuario] Fecha de registro generada: {}", usuario.getFecha_registro());
        }

        try {
            logger.info("[guardarUsuario] 🔹 Llamando a repository.save()...");
            Usuario guardado = usuariosRepository.save(usuario);
            
            if (guardado == null) {
                logger.error("[guardarUsuario] ❌ Repository.save() retornó NULL");
                throw new RuntimeException("Repository retornó null");
            }
            
            logger.info("[guardarUsuario] ✅ Usuario guardado exitosamente: id={}, email={}", 
                guardado.getId(), guardado.getEmail());
            return guardado;
            
        } catch (Exception e) {
            logger.error("[guardarUsuario] ❌ ERROR COMPLETO:", e);
            logger.error("[guardarUsuario] ❌ Mensaje: {}", e.getMessage());
            logger.error("[guardarUsuario] ❌ Clase: {}", e.getClass().getName());
            
            throw new RuntimeException("Error guardando usuario: " + e.getMessage(), e);
        }
    }

    public Page<Usuario> obtenerUsuarios(int page, int size) {
        logger.info("Obteniendo usuarios: page={}, size={}", page, size);
        return usuariosRepository.findAll(PageRequest.of(page, size));
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        logger.info("Obteniendo todos los usuarios");
        return usuariosRepository.findAll();
    }

    public Usuario buscarPorEmail(String email) {
        logger.info("Buscando usuario por email: {}", email);
        return usuariosRepository.findByEmail(email);
    }

    public Usuario buscarPorId(String id) {
        logger.info("Buscando usuario por id: {}", id);
        return usuariosRepository.findById(id);
    }

    public void eliminarUsuario(String id) {
        logger.info("Eliminando usuario por id: {}", id);
        usuariosRepository.deleteById(id);
    }

    public long contarPorRol(String rol_id) {
        logger.info("Contando usuarios por rol: {}", rol_id);
        return usuariosRepository.countByRolId(rol_id);
    }

    public Usuario login(String email, String password) {
        Usuario usuario = usuariosRepository.findByEmail(email);
        logger.info("Login - Email: {}", email);
        boolean match = usuario != null && passwordEncoder.matches(password, usuario.getPassword());
        logger.info("Login - Password match: {}", match);
        return match ? usuario : null;
    }
}
