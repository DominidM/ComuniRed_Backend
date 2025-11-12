package com.comunired.usuarios.application.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import com.comunired.usuarios.application.dto.AuthPayload;
import com.comunired.usuarios.application.dto.UsuariosDTO;
import com.comunired.usuarios.security.JwtUtil;

@Service
public class AuthService {

    private final UsuariosRepository usuariosRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsuariosService usuariosService;

    @Autowired
    public AuthService(UsuariosRepository usuariosRepository, 
                       JwtUtil jwtUtil,
                       UsuariosService usuariosService) {
        this.usuariosRepository = usuariosRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.usuariosService = usuariosService;
    }

    public AuthPayload login(String email, String plainPassword) {
        Usuario usuario = usuariosRepository.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Credenciales inválidas");
        }

        boolean matches = passwordEncoder.matches(plainPassword, usuario.getPassword());
        if (!matches) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Actualizar última actividad
        usuario.setUltimaActividad(Instant.now());
        Usuario usuarioGuardado = usuariosRepository.save(usuario);

        // Generar token
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol_id", usuario.getRol_id());
        claims.put("email", usuario.getEmail());
        String token = jwtUtil.generateToken(usuario.getId(), claims);

        UsuariosDTO usuarioDTO = usuariosService.toDTO(usuarioGuardado);

        return new AuthPayload(token, usuarioDTO);
    }
    
    public void actualizarUltimaActividad(String usuarioId) {
        Usuario usuario = usuariosRepository.findById(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        usuario.setUltimaActividad(Instant.now());
        usuariosRepository.save(usuario);
    }
}
