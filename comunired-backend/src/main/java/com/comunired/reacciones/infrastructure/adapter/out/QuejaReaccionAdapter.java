package com.comunired.reacciones.infrastructure.adapter.out;

import com.comunired.quejas.application.port.out.QuejaOutPorts.EstadoQuejaPort;
import com.comunired.quejas.application.port.out.QuejaOutPorts.QuejaRepositoryPort;
import com.comunired.reacciones.application.port.out.QuejaParaReaccionPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador de salida del dominio reacciones hacia quejas.
 * Reacciones habla con este adapter — nunca con el repo de quejas directamente.
 */
@Component
public class QuejaReaccionAdapter implements QuejaParaReaccionPort {

    private final QuejaRepositoryPort quejaRepository;
    private final EstadoQuejaPort estadoPort;

    public QuejaReaccionAdapter(QuejaRepositoryPort quejaRepository,
                                 EstadoQuejaPort estadoPort) {
        this.quejaRepository = quejaRepository;
        this.estadoPort = estadoPort;
    }

    @Override
    public Optional<String> obtenerEstadoClave(String quejaId) {
        return quejaRepository.buscarPorId(quejaId)
                .flatMap(q -> estadoPort.buscarPorId(q.getEstadoId()))
                .map(EstadoQuejaPort.EstadoInfo::clave);
    }

    @Override
    public boolean existe(String quejaId) {
        return quejaRepository.existePorId(quejaId);
    }
}
