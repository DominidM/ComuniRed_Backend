package com.comunired.estados_queja.infrastructure.adapter.out.quejabridge;

import com.comunired.estados_queja.infrastructure.repository.Estados_quejaMongoRepository;
import com.comunired.quejas.application.port.out.QuejaOutPorts.EstadoQuejaPort;
import com.comunired.shared.infrastructure.cache.TimedCache;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EstadoQuejaAdapter implements EstadoQuejaPort {

    private static final long CACHE_TTL_MS = 60_000;

    private final Estados_quejaMongoRepository repo;
    private final TimedCache<String, EstadoInfo> cache = new TimedCache<>(CACHE_TTL_MS);

    public EstadoQuejaAdapter(Estados_quejaMongoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<EstadoInfo> buscarPorClave(String clave) {
        var cached = cache.get(clave);
        if (cached != null) return Optional.of(cached);
        return repo.findByClave(clave)
                .map(e -> {
                    var info = new EstadoInfo(e.getId(), e.getClave(), e.getNombre());
                    cache.put(e.getId(), info);
                    return info;
                });
    }

    @Override
    public Optional<EstadoInfo> buscarPorId(String id) {
        var cached = cache.get(id);
        if (cached != null) return Optional.of(cached);
        return repo.findById(id)
                .map(e -> {
                    var info = new EstadoInfo(e.getId(), e.getClave(), e.getNombre());
                    cache.put(id, info);
                    return info;
                });
    }

    @Override
    public Map<String, EstadoInfo> buscarPorIds(List<String> ids) {
        var missing = ids.stream()
                .filter(id -> cache.get(id) == null)
                .distinct()
                .toList();
        if (!missing.isEmpty()) {
            repo.findAllById(missing).forEach(e ->
                    cache.put(e.getId(), new EstadoInfo(e.getId(), e.getClave(), e.getNombre()))
            );
        }
        var result = new java.util.HashMap<String, EstadoInfo>();
        for (var id : ids.stream().distinct().toList()) {
            var val = cache.get(id);
            if (val != null) result.put(id, val);
        }
        return result;
    }
}