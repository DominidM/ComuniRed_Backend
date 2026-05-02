package com.comunired.quejas.application.service.command;

import com.comunired.quejas.application.dto.in.QuejaCommands.CambiarEstadoCommand;
import com.comunired.quejas.application.port.in.QuejaPorts.CambiarEstadoQuejaUseCase;
import com.comunired.reacciones.application.service.ReaccionesService.VotosMinimosAlcanzadosEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener que recibe VotosMinimosAlcanzadosEvent del dominio reacciones
 * y dispara el cambio de estado en quejas.
 *
 * Así reacciones no importa nada de quejas — la comunicación va por evento.
 */
@Component
public class VotosMinimosListener {

    private final CambiarEstadoQuejaUseCase cambiarEstado;

    public VotosMinimosListener(CambiarEstadoQuejaUseCase cambiarEstado) {
        this.cambiarEstado = cambiarEstado;
    }

    @EventListener
    public void onVotosMinimosAlcanzados(VotosMinimosAlcanzadosEvent event) {
        try {
            cambiarEstado.ejecutar(new CambiarEstadoCommand(
                    event.quejaId(),
                    "sistema",
                    "PENDIENTE",
                    "Mínimo de votos alcanzado (" + event.votosAccept() + ")"
            ));
        } catch (Exception e) {
            System.out.println("⚠️ Error al cambiar estado por votos mínimos: " + e.getMessage());
        }
    }
}
