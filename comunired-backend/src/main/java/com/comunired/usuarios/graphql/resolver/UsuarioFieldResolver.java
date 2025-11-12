package com.comunired.usuarios.graphql.resolver;

import com.comunired.usuarios.application.dto.UsuariosDTO;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import java.time.Instant;
import java.time.Duration;

@Controller
public class UsuarioFieldResolver {
    
    /**
     * Calcula si el usuario está en línea basado en ultimaActividad
     * Se considera "en línea" si la última actividad fue hace menos de 5 minutos
     */
    @SchemaMapping(typeName = "Usuario", field = "estaEnLinea")
    public Boolean estaEnLinea(UsuariosDTO usuario) {  // 👈 Cambiar de Usuario a UsuariosDTO
        if (usuario.getUltimaActividad() == null) {
            return false;
        }
        
        Instant ahora = Instant.now();
        Instant ultimaActividad = usuario.getUltimaActividad();
        
        // Calcular diferencia en minutos
        long minutosInactivo = Duration.between(ultimaActividad, ahora).toMinutes();
        
        // En línea si estuvo activo hace menos de 5 minutos
        return minutosInactivo < 5;
    }
}
