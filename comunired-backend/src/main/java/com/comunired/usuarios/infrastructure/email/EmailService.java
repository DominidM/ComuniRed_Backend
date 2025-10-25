package com.comunired.usuarios.infrastructure.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Envía un email con el código de recuperación de contraseña
     */
    public void enviarCodigoRecuperacion(String destinatario, String codigo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinatario);
            message.setSubject("Código de recuperación de contraseña - ComuniRed");
            message.setText(
                "Hola,\n\n" +
                "Has solicitado recuperar tu contraseña en ComuniRed.\n\n" +
                "Tu código de verificación es: " + codigo + "\n\n" +
                "Este código expirará en 15 minutos.\n\n" +
                "Si no solicitaste este cambio, ignora este mensaje.\n\n" +
                "Saludos,\n" +
                "Equipo ComuniRed"
            );
            
            mailSender.send(message);
            logger.info("Código de recuperación enviado a: {}", destinatario);
        } catch (Exception e) {
            logger.error("Error al enviar email a {}", destinatario, e);
            throw new RuntimeException("No se pudo enviar el email de recuperación");
        }
    }
}