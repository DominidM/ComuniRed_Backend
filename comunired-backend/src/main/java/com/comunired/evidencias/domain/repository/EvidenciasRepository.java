package com.comunired.evidencias.domain.repository;

import com.comunired.evidencias.domain.entity.Evidencias;
import java.util.List;
import java.util.Optional;

public interface EvidenciasRepository {
    Evidencias save(Evidencias evidencia);
    Optional<Evidencias> findById(String id);
    List<Evidencias> findAll();
    List<Evidencias> findByQuejaId(String quejaId);
    void deleteById(String id);
    void deleteByQuejaId(String quejaId);
    boolean existsById(String id);
}
