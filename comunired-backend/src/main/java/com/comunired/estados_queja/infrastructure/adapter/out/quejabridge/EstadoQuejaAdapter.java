package com.comunired.estados_queja.infrastructure.adapter.out.quejabridge;

import com.comunired.estados_queja.infrastructure.repository.Estados_quejaMongoRepository;
import com.comunired.quejas.application.port.out.QuejaOutPorts.EstadoQuejaPort;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class EstadoQuejaAdapter implements EstadoQuejaPort {

    private final Estados_quejaMongoRepository repo;

    public EstadoQuejaAdapter(Estados_quejaMongoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<EstadoInfo> buscarPorClave(String clave) {
        return repo.findAll().stream()
                .filter(e -> clave.equals(e.getClave()))
                .findFirst()
                .map(e -> new EstadoInfo(e.getId(), e.getClave(), e.getNombre()));
    }

    @Override
    public Optional<EstadoInfo> buscarPorId(String id) {
        return repo.findById(id)
                .map(e -> new EstadoInfo(e.getId(), e.getClave(), e.getNombre()));
    }
}