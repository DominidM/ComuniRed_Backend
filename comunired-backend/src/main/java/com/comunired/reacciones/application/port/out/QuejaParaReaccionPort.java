package com.comunired.reacciones.application.port.out;

import java.util.Optional;

/**
 * Lo que el dominio reacciones necesita saber de quejas.
 * Reacciones no importa nada de quejas directamente — usa este port.
 */
public interface QuejaParaReaccionPort {

    /**
     * Obtiene el estado actual (clave) de una queja.
     * Ejemplo: "VOTACION", "PENDIENTE", "APROBADA"
     */
    Optional<String> obtenerEstadoClave(String quejaId);

    /**
     * Verifica que la queja existe.
     */
    boolean existe(String quejaId);
}
