package com.comunired.quejas.domain.repository;

import com.comunired.quejas.domain.entity.Quejas;
import java.util.List;
import java.util.Optional;

public interface QuejasRepository {
    Quejas save(Quejas quejas);
    Optional<Quejas> findById(String id);
    List<Quejas> findAll();
    List<Quejas> findByUsuarioId(String usuarioId);
    List<Quejas> findByCategoriaId(String categoriaId);
    List<Quejas> findByEstadoId(String estadoId);
    void deleteById(String id);
    boolean existsById(String id);
    long count();
}
