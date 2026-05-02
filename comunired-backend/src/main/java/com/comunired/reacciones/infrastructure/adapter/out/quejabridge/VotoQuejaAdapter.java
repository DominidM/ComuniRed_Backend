package com.comunired.reacciones.infrastructure.adapter.out.quejabridge;

import com.comunired.quejas.application.port.out.QuejaOutPorts.VotoQuejaPort;
import com.comunired.reacciones.infrastructure.repository.ReaccionesMongoRepository;
import com.comunired.tipos_reaccion.infrastructure.repository.Tipos_reaccionMongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VotoQuejaAdapter implements VotoQuejaPort {

    private static final String KEY_ACCEPT = "accept";
    private static final String KEY_REJECT = "reject";

    private final ReaccionesMongoRepository reaccionesRepo;
    private final Tipos_reaccionMongoRepository tiposRepo;

    public VotoQuejaAdapter(ReaccionesMongoRepository reaccionesRepo,
                             Tipos_reaccionMongoRepository tiposRepo) {
        this.reaccionesRepo = reaccionesRepo;
        this.tiposRepo = tiposRepo;
    }

    @Override
    public long contarVotosSi(String quejaId) {
        return tiposRepo.findByKey(KEY_ACCEPT)
                .map(t -> reaccionesRepo.countByQuejaIdAndTipoReaccionId(quejaId, t.getId()))
                .orElse(0L);
    }

    @Override
    public long contarVotosNo(String quejaId) {
        return tiposRepo.findByKey(KEY_REJECT)
                .map(t -> reaccionesRepo.countByQuejaIdAndTipoReaccionId(quejaId, t.getId()))
                .orElse(0L);
    }

    @Override
    public boolean yaVoto(String quejaId, String usuarioId) {
        return tiposRepo.findByKey(KEY_ACCEPT)
                .or(() -> tiposRepo.findByKey(KEY_REJECT))
                .map(t -> reaccionesRepo.findByQuejaIdAndUsuarioId(quejaId, usuarioId).isPresent())
                .orElse(false);
    }

    @Override
    public Optional<String> obtenerVotoUsuario(String quejaId, String usuarioId) {
        return reaccionesRepo.findByQuejaIdAndUsuarioId(quejaId, usuarioId)
                .flatMap(r -> tiposRepo.findById(r.getTipo_reaccion_id()))
                .filter(t -> KEY_ACCEPT.equals(t.getKey()) || KEY_REJECT.equals(t.getKey()))
                .map(t -> t.getKey());
    }
}