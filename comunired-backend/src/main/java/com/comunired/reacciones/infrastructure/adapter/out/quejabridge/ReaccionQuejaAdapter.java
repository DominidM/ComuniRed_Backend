package com.comunired.reacciones.infrastructure.adapter.out.quejabridge;

import com.comunired.quejas.application.port.out.QuejaOutPorts.ReaccionQuejaPort;
import com.comunired.reacciones.infrastructure.model.ReaccionesModel;
import com.comunired.reacciones.infrastructure.repository.ReaccionesMongoRepository;
import com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion;
import com.comunired.tipos_reaccion.infrastructure.repository.Tipos_reaccionMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReaccionQuejaAdapter implements ReaccionQuejaPort {

    private final ReaccionesMongoRepository repo;
    private final Tipos_reaccionMongoRepository tiposRepo;

    public ReaccionQuejaAdapter(ReaccionesMongoRepository repo,
                                 Tipos_reaccionMongoRepository tiposRepo) {
        this.repo = repo;
        this.tiposRepo = tiposRepo;
    }

    private Map<String, String> obtenerMapaIdAKey() {
        return tiposRepo.findAll().stream()
                .filter(Tipos_reaccion::isActivo)
                .collect(Collectors.toMap(Tipos_reaccion::getId, Tipos_reaccion::getKey));
    }

    @Override
    public Map<String, Long> contarReacciones(String quejaId) {
        Map<String, String> idAKey = obtenerMapaIdAKey();
        return repo.findByQuejaId(quejaId)
                .stream()
                .collect(Collectors.groupingBy(
                        r -> idAKey.getOrDefault(r.getTipo_reaccion_id(), r.getTipo_reaccion_id()),
                        Collectors.counting()
                ));
    }

    @Override
    public Optional<String> obtenerReaccionUsuario(String quejaId, String usuarioId) {
        Map<String, String> idAKey = obtenerMapaIdAKey();
        return repo.findByQuejaIdAndUsuarioId(quejaId, usuarioId)
                .map(r -> idAKey.getOrDefault(r.getTipo_reaccion_id(), r.getTipo_reaccion_id()));
    }

    @Override
    public Map<String, Map<String, Long>> contarReaccionesPorQuejaIds(List<String> quejaIds) {
        if (quejaIds.isEmpty()) return Map.of();
        Map<String, String> idAKey = obtenerMapaIdAKey();
        return repo.findByQuejaIdIn(quejaIds).stream()
                .collect(Collectors.groupingBy(
                        ReaccionesModel::getQueja_id,
                        Collectors.groupingBy(
                                r -> idAKey.getOrDefault(r.getTipo_reaccion_id(), r.getTipo_reaccion_id()),
                                Collectors.counting()
                        )
                ));
    }

    @Override
    public Map<String, String> obtenerReaccionesUsuarioPorQuejaIds(List<String> quejaIds, String usuarioId) {
        if (quejaIds.isEmpty()) return Map.of();
        Map<String, String> idAKey = obtenerMapaIdAKey();
        return repo.findByQuejaIdInAndUsuarioId(quejaIds, usuarioId).stream()
                .collect(Collectors.toMap(
                        ReaccionesModel::getQueja_id,
                        r -> idAKey.getOrDefault(r.getTipo_reaccion_id(), r.getTipo_reaccion_id())
                ));
    }
}