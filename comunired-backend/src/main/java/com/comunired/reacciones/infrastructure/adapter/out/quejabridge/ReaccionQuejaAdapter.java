package com.comunired.reacciones.infrastructure.adapter.out.quejabridge;

import com.comunired.quejas.application.port.out.QuejaOutPorts.ReaccionQuejaPort;
import com.comunired.reacciones.infrastructure.model.ReaccionesModel;
import com.comunired.reacciones.infrastructure.repository.ReaccionesMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReaccionQuejaAdapter implements ReaccionQuejaPort {

    private final ReaccionesMongoRepository repo;

    public ReaccionQuejaAdapter(ReaccionesMongoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Map<String, Long> contarReacciones(String quejaId) {
        return repo.findByQuejaId(quejaId)
                .stream()
                .collect(Collectors.groupingBy(
                        r -> r.getTipo_reaccion_id(),
                        Collectors.counting()
                ));
    }

    @Override
    public Optional<String> obtenerReaccionUsuario(String quejaId, String usuarioId) {
        return repo.findByQuejaIdAndUsuarioId(quejaId, usuarioId)
                .map(r -> r.getTipo_reaccion_id());
    }

    @Override
    public Map<String, Map<String, Long>> contarReaccionesPorQuejaIds(List<String> quejaIds) {
        if (quejaIds.isEmpty()) return Map.of();
        return repo.findByQuejaIdIn(quejaIds).stream()
                .collect(Collectors.groupingBy(
                        ReaccionesModel::getQueja_id,
                        Collectors.groupingBy(
                                ReaccionesModel::getTipo_reaccion_id,
                                Collectors.counting()
                        )
                ));
    }

    @Override
    public Map<String, String> obtenerReaccionesUsuarioPorQuejaIds(List<String> quejaIds, String usuarioId) {
        if (quejaIds.isEmpty()) return Map.of();
        return repo.findByQuejaIdInAndUsuarioId(quejaIds, usuarioId).stream()
                .collect(Collectors.toMap(
                        ReaccionesModel::getQueja_id,
                        ReaccionesModel::getTipo_reaccion_id
                ));
    }
}