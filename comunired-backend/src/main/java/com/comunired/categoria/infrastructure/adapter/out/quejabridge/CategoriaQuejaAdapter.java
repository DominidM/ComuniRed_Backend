package com.comunired.categoria.infrastructure.adapter.out.quejabridge;

import com.comunired.categoria.infrastructure.repository.CategoriaMongoRepository;
import com.comunired.quejas.application.port.out.QuejaOutPorts.CategoriaQuejaPort;
import com.comunired.shared.infrastructure.cache.TimedCache;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoriaQuejaAdapter implements CategoriaQuejaPort {

    private static final long CACHE_TTL_MS = 60_000;

    private final CategoriaMongoRepository repo;
    private final TimedCache<String, CategoriaInfo> cache = new TimedCache<>(CACHE_TTL_MS);

    public CategoriaQuejaAdapter(CategoriaMongoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<CategoriaInfo> buscarPorId(String categoriaId) {
        var cached = cache.get(categoriaId);
        if (cached != null) return Optional.of(cached);
        return repo.findById(categoriaId)
                .map(c -> {
                    var info = new CategoriaInfo(c.getId(), c.getNombre(), c.getDescripcion());
                    cache.put(categoriaId, info);
                    return info;
                });
    }

    @Override
    public Map<String, CategoriaInfo> buscarPorIds(List<String> ids) {
        var missing = ids.stream()
                .filter(id -> cache.get(id) == null)
                .distinct()
                .toList();
        if (!missing.isEmpty()) {
            repo.findAllById(missing).forEach(c ->
                    cache.put(c.getId(), new CategoriaInfo(c.getId(), c.getNombre(), c.getDescripcion()))
            );
        }
        var result = new java.util.HashMap<String, CategoriaInfo>();
        for (var id : ids.stream().distinct().toList()) {
            var val = cache.get(id);
            if (val != null) result.put(id, val);
        }
        return result;
    }
}