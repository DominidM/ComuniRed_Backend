package com.comunired.comentarios.infrastructure.adapter.out.quejabridge;

import com.comunired.comentarios.infrastructure.model.ComentariosModel;
import com.comunired.comentarios.infrastructure.repository.ComentariosMongoRepository;
import com.comunired.quejas.application.port.out.QuejaOutPorts.ComentarioQuejaPort;
import com.comunired.usuarios.infrastructure.repository.UsuarioMongoRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ComentarioQuejaAdapter implements ComentarioQuejaPort {

    private final ComentariosMongoRepository comentariosRepo;
    private final UsuarioMongoRepository usuarioRepo;

    public ComentarioQuejaAdapter(ComentariosMongoRepository comentariosRepo,
                                   UsuarioMongoRepository usuarioRepo) {
        this.comentariosRepo = comentariosRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public List<ComentarioInfo> buscarPorQueja(String quejaId) {
        return comentariosRepo.findByQuejaIdActivosOrderByFechaCreacionAsc(quejaId)
                .stream()
                .map(c -> {
                    var usuario = usuarioRepo.findById(c.getUsuario_id()).orElse(null);
                    return new ComentarioInfo(
                            c.getId(),
                            c.getTexto(),
                            c.getUsuario_id(),
                            usuario != null ? usuario.getNombre() : "",
                            usuario != null ? usuario.getApellido() : "",
                            usuario != null ? usuario.getFoto_perfil() : null,
                            c.getFecha_creacion()
                    );
                })
                .toList();
    }
}