package com.comunired.usuarios.infrastructure.adapter.out.quejabridge;

import com.comunired.quejas.application.port.out.QuejaOutPorts.UsuarioQuejaPort;
import com.comunired.shared.infrastructure.cache.TimedCache;
import com.comunired.usuarios.infrastructure.repository.UsuarioMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsuarioQuejaAdapter implements UsuarioQuejaPort {

    private static final long CACHE_TTL_MS = 30_000;

    private final UsuarioMongoRepository repo;
    private final TimedCache<String, UsuarioInfo> cache = new TimedCache<>(CACHE_TTL_MS);

    public UsuarioQuejaAdapter(UsuarioMongoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<UsuarioInfo> buscarPorId(String usuarioId) {
        var cached = cache.get(usuarioId);
        if (cached != null) return Optional.of(cached);
        return repo.findById(usuarioId)
                .map(u -> {
                    var info = new UsuarioInfo(u.getId(), u.getNombre(), u.getApellido(), u.getFoto_perfil());
                    cache.put(usuarioId, info);
                    return info;
                });
    }

    @Override
    public Map<String, UsuarioInfo> buscarPorIds(List<String> ids) {
        var missing = ids.stream()
                .filter(id -> cache.get(id) == null)
                .distinct()
                .toList();
        if (!missing.isEmpty()) {
            repo.findAllById(missing).forEach(u ->
                    cache.put(u.getId(), new UsuarioInfo(u.getId(), u.getNombre(), u.getApellido(), u.getFoto_perfil()))
            );
        }
        var result = new java.util.HashMap<String, UsuarioInfo>();
        for (var id : ids.stream().distinct().toList()) {
            var val = cache.get(id);
            if (val != null) result.put(id, val);
        }
        return result;
    }
}