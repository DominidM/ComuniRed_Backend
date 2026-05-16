package com.comunired.comentarios.infrastructure.adapter.out.quejabridge;

import com.comunired.comentarios.infrastructure.model.ComentariosModel;
import com.comunired.comentarios.infrastructure.repository.ComentariosMongoRepository;
import com.comunired.quejas.application.port.out.QuejaOutPorts.ComentarioQuejaPort;
import com.comunired.usuarios.infrastructure.repository.UsuarioMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<ComentariosModel> comentarios = comentariosRepo
                .findByQuejaIdActivosOrderByFechaCreacionAsc(quejaId);
        if (comentarios.isEmpty()) return List.of();

        List<String> userIds = comentarios.stream()
                .map(ComentariosModel::getUsuario_id)
                .distinct()
                .toList();
        Map<String, com.comunired.usuarios.domain.entity.Usuario> usuarios =
                usuarioRepo.findAllById(userIds).stream()
                        .collect(Collectors.toMap(u -> u.getId(), u -> u, (a, b) -> a));

        return comentarios.stream()
                .map(c -> {
                    var usuario = usuarios.get(c.getUsuario_id());
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

    @Override
    public Map<String, List<ComentarioInfo>> buscarPorQuejaIds(List<String> quejaIds) {
        if (quejaIds.isEmpty()) return Map.of();

        List<ComentariosModel> todos = comentariosRepo.findByQuejaIdInActivos(quejaIds);
        if (todos.isEmpty()) {
            Map<String, List<ComentarioInfo>> empty = new java.util.HashMap<>();
            for (String qid : quejaIds) empty.put(qid, List.of());
            return empty;
        }

        List<String> userIds = todos.stream()
                .map(ComentariosModel::getUsuario_id)
                .distinct()
                .toList();
        Map<String, com.comunired.usuarios.domain.entity.Usuario> usuarios =
                usuarioRepo.findAllById(userIds).stream()
                        .collect(Collectors.toMap(u -> u.getId(), u -> u, (a, b) -> a));

        Map<String, List<ComentarioInfo>> grouped = todos.stream()
                .collect(Collectors.groupingBy(
                        ComentariosModel::getQueja_id,
                        Collectors.mapping(c -> {
                            var u = usuarios.get(c.getUsuario_id());
                            return new ComentarioInfo(
                                    c.getId(), c.getTexto(),
                                    c.getUsuario_id(),
                                    u != null ? u.getNombre() : "",
                                    u != null ? u.getApellido() : "",
                                    u != null ? u.getFoto_perfil() : null,
                                    c.getFecha_creacion()
                            );
                        }, Collectors.toList())
                ));

        for (String qid : quejaIds) {
            grouped.putIfAbsent(qid, List.of());
        }
        return grouped;
    }
}