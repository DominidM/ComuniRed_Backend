package com.comunired.reacciones.infrastructure.adapter.out.quejabridge;

import com.comunired.quejas.application.port.out.QuejaOutPorts.VotoQuejaPort;
import com.comunired.reacciones.infrastructure.repository.ReaccionesMongoRepository;
import com.comunired.tipos_reaccion.infrastructure.repository.Tipos_reaccionMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class VotoQuejaAdapter implements VotoQuejaPort {

    private static final String KEY_ACCEPT = "accept";
    private static final String KEY_REJECT = "reject";

    private final ReaccionesMongoRepository reaccionesRepo;
    private final Tipos_reaccionMongoRepository tiposRepo;
    private final Map<String, String> tipoIdCache = new ConcurrentHashMap<>();

    public VotoQuejaAdapter(ReaccionesMongoRepository reaccionesRepo,
                             Tipos_reaccionMongoRepository tiposRepo) {
        this.reaccionesRepo = reaccionesRepo;
        this.tiposRepo = tiposRepo;
    }

    private String obtenerTipoId(String key) {
        return tipoIdCache.computeIfAbsent(key, k ->
                tiposRepo.findByKey(k).map(t -> t.getId()).orElse(null));
    }

    @Override
    public long contarVotosSi(String quejaId) {
        String id = obtenerTipoId(KEY_ACCEPT);
        return id != null ? reaccionesRepo.countByQuejaIdAndTipoReaccionId(quejaId, id) : 0L;
    }

    @Override
    public long contarVotosNo(String quejaId) {
        String id = obtenerTipoId(KEY_REJECT);
        return id != null ? reaccionesRepo.countByQuejaIdAndTipoReaccionId(quejaId, id) : 0L;
    }

    @Override
    public boolean yaVoto(String quejaId, String usuarioId) {
        return reaccionesRepo.findByQuejaIdAndUsuarioId(quejaId, usuarioId).isPresent();
    }

    @Override
    public Optional<String> obtenerVotoUsuario(String quejaId, String usuarioId) {
        return reaccionesRepo.findByQuejaIdAndUsuarioId(quejaId, usuarioId)
                .flatMap(r -> tiposRepo.findById(r.getTipo_reaccion_id()))
                .filter(t -> KEY_ACCEPT.equals(t.getKey()) || KEY_REJECT.equals(t.getKey()))
                .map(t -> t.getKey());
    }

    @Override
    public Map<String, VotoCounts> contarVotosPorQuejaIds(List<String> quejaIds) {
        if (quejaIds.isEmpty()) return Map.of();
        String acceptId = obtenerTipoId(KEY_ACCEPT);
        String rejectId = obtenerTipoId(KEY_REJECT);
        if (acceptId == null || rejectId == null) return Map.of();

        List<com.comunired.reacciones.infrastructure.model.ReaccionesModel> todos =
                reaccionesRepo.findByQuejaIdIn(quejaIds);

        Map<String, long[]> acc = new java.util.HashMap<>();
        for (var r : todos) {
            var arr = acc.computeIfAbsent(r.getQueja_id(), k -> new long[2]);
            if (acceptId.equals(r.getTipo_reaccion_id())) arr[0]++;
            else if (rejectId.equals(r.getTipo_reaccion_id())) arr[1]++;
        }
        Map<String, VotoCounts> result = new java.util.HashMap<>();
        for (String qid : quejaIds) {
            var arr = acc.get(qid);
            result.put(qid, arr != null ? new VotoCounts(arr[0], arr[1]) : new VotoCounts(0, 0));
        }
        return result;
    }

    @Override
    public Map<String, String> obtenerVotosUsuarioPorQuejaIds(List<String> quejaIds, String usuarioId) {
        if (quejaIds.isEmpty()) return Map.of();
        String acceptId = obtenerTipoId(KEY_ACCEPT);
        String rejectId = obtenerTipoId(KEY_REJECT);
        if (acceptId == null || rejectId == null) return Map.of();

        return reaccionesRepo.findByQuejaIdInAndUsuarioId(quejaIds, usuarioId).stream()
                .filter(r -> acceptId.equals(r.getTipo_reaccion_id()) || rejectId.equals(r.getTipo_reaccion_id()))
                .collect(Collectors.toMap(
                        com.comunired.reacciones.infrastructure.model.ReaccionesModel::getQueja_id,
                        r -> acceptId.equals(r.getTipo_reaccion_id()) ? KEY_ACCEPT : KEY_REJECT
                ));
    }
}