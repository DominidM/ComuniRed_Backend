package com.comunired.usuarios.application.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import com.comunired.usuarios.infrastructure.email.EmailService;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private static final int EXPIRATION_MINUTES = 15;
    private static final int CODE_LENGTH = 6;

    private final UsuariosRepository usuariosRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(
            UsuariosRepository usuariosRepository, 
            EmailService emailService) {
        this.usuariosRepository = usuariosRepository;
        this.emailService = emailService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Genera un código de 6 dígitos aleatorio
     */
    private String generarCodigoAleatorio() {
        SecureRandom random = new SecureRandom();
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }

    /**
     * Solicita un código de recuperación y lo envía por email
     * @param email Email del usuario
     * @return true si se envió exitosamente
     */
    public boolean solicitarCodigoRecuperacion(String email) {
        try {
            Usuario usuario = usuariosRepository.findByEmail(email);
            
            if (usuario == null) {
                logger.warn("Intento de recuperación para email no registrado: {}", email);
                // Por seguridad, no revelar si el email existe o no
                return true;
            }

            String codigo = generarCodigoAleatorio();
            LocalDateTime expiry = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

            // Guardar código y fecha de expiración
            usuario.setResetPasswordToken(codigo);
            usuario.setResetPasswordExpiry(expiry);
            usuariosRepository.save(usuario);

            // Enviar email
            emailService.enviarCodigoRecuperacion(email, codigo);
            
            logger.info("Código de recuperación generado para usuario: {}", email);
            return true;

        } catch (Exception e) {
            logger.error("Error al solicitar código de recuperación para {}", email, e);
            throw new RuntimeException("Error al procesar la solicitud de recuperación");
        }
    }

    /**
     * Verifica si un código es válido para un email
     * @param email Email del usuario
     * @param codigo Código a verificar
     * @return true si el código es válido y no ha expirado
     */
    public boolean verificarCodigo(String email, String codigo) {
        try {
            Usuario usuario = usuariosRepository.findByEmail(email);
            
            if (usuario == null) {
                logger.warn("Intento de verificación para email no registrado: {}", email);
                return false;
            }

            if (usuario.getResetPasswordToken() == null || 
                usuario.getResetPasswordExpiry() == null) {
                logger.warn("Usuario {} no tiene código de recuperación activo", email);
                return false;
            }

            // Verificar si el código coincide
            if (!codigo.equals(usuario.getResetPasswordToken())) {
                logger.warn("Código incorrecto para usuario: {}", email);
                return false;
            }

            // Verificar si no ha expirado
            if (LocalDateTime.now().isAfter(usuario.getResetPasswordExpiry())) {
                logger.warn("Código expirado para usuario: {}", email);
                return false;
            }

            logger.info("Código verificado correctamente para usuario: {}", email);
            return true;

        } catch (Exception e) {
            logger.error("Error al verificar código para {}", email, e);
            return false;
        }
    }

    /**
     * Cambia la contraseña del usuario después de verificar el código
     * @param email Email del usuario
     * @param codigo Código de verificación
     * @param nuevaPassword Nueva contraseña
     * @return true si se cambió exitosamente
     */
    public boolean cambiarPasswordConCodigo(String email, String codigo, String nuevaPassword) {
        try {
            if (!verificarCodigo(email, codigo)) {
                logger.warn("Intento de cambio de contraseña con código inválido: {}", email);
                return false;
            }

            Usuario usuario = usuariosRepository.findByEmail(email);
            
            String hashedPassword = passwordEncoder.encode(nuevaPassword);
            usuario.setPassword(hashedPassword);

            usuario.setResetPasswordToken(null);
            usuario.setResetPasswordExpiry(null);

            usuariosRepository.save(usuario);
            
            logger.info("Contraseña actualizada exitosamente para usuario: {}", email);
            return true;

        } catch (Exception e) {
            logger.error("Error al cambiar contraseña para {}", email, e);
            throw new RuntimeException("Error al cambiar la contraseña");
        }
    }
}