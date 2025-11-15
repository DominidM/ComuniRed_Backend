package com.comunired.comentarios.domain.repository;

import com.comunired.comentarios.domain.entity.Comentarios;
import java.util.List;
import java.util.Optional;

public interface ComentariosRepository {
    Comentarios save(Comentarios comentario);
    Optional<Comentarios> findById(String id);
    List<Comentarios> findAll();
    List<Comentarios> findByQuejaId(String quejaId);
    List<Comentarios> findByUsuarioId(String usuarioId);
    long countByQuejaId(String quejaId);
    void deleteById(String id);
    boolean existsById(String id);
}
