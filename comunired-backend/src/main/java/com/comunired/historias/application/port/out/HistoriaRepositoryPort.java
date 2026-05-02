package com.comunired.historias.application.port.out;

import com.comunired.historias.domain.entity.Historia;
import java.util.List;
import java.util.Optional;

public interface HistoriaRepositoryPort {
    Historia guardar(Historia historia);
    Optional<Historia> buscarPorId(String id);
    List<Historia> buscarActivas();
    void guardarActualizada(Historia historia);
}