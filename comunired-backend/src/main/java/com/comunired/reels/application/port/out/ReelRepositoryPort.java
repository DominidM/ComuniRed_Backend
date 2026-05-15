package com.comunired.reels.application.port.out;

import com.comunired.reels.domain.entity.Reel;

import java.util.List;
import java.util.Optional;

public interface ReelRepositoryPort {
    Reel guardar(Reel reel);
    Optional<Reel> buscarPorId(String id);
    List<Reel> buscarActivos();
    List<Reel> buscarTodos();
    List<Reel> buscarPorTermino(String termino);
    void eliminar(String id);
}
