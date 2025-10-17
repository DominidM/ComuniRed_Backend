package com.comunired.usuarios.application.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import com.comunired.usuarios.application.dto.AuthPayload;
import com.comunired.usuarios.security.JwtUtil;

@Service
public class AuthService {

    private final UsuariosRepository usuariosRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UsuariosRepository usuariosRepository, JwtUtil jwtUtil) {
        this.usuariosRepository = usuariosRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Valida credenciales y devuelve un AuthPayload con token + usuario (sin password).
     * Lanza RuntimeException si credenciales inválidas (cámbialo por excepciones específicas si quieres).
     */
    public AuthPayload login(String email, String plainPassword) {
        Usuario usuario = usuariosRepository.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Credenciales inválidas");
        }

        boolean matches = passwordEncoder.matches(plainPassword, usuario.getPassword());
        if (!matches) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Prepara claims (añade lo que necesites)
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol_id", usuario.getRol_id());
        claims.put("email", usuario.getEmail());

        // Genera token
        String token = jwtUtil.generateToken(usuario.getId(), claims);

        // No devolver password al cliente
        usuario.setPassword(null);

        return new AuthPayload(token, usuario);
    }
}