package com.comunired.usuarios.infrastructure.adapter.out.quejabridge;

import com.comunired.quejas.application.port.out.QuejaOutPorts.UsuarioQuejaPort;
import com.comunired.usuarios.infrastructure.repository.UsuarioMongoRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UsuarioQuejaAdapter implements UsuarioQuejaPort {

    private final UsuarioMongoRepository repo;

    public UsuarioQuejaAdapter(UsuarioMongoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<UsuarioInfo> buscarPorId(String usuarioId) {
        return repo.findById(usuarioId)
                .map(u -> new UsuarioInfo(
                        u.getId(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getFoto_perfil()
                ));
    }
}