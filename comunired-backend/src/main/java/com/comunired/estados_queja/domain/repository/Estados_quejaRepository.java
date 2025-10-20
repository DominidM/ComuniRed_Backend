package com.comunired.estados_queja.domain.repository;

import java.util.List;
import java.util.Optional;
import com.comunired.estados_queja.domain.entity.Estados_queja;

public interface Estados_quejaRepository {

    Estados_queja guardar(Estados_queja estado);

    Estados_queja modificar(Estados_queja estado);

    Optional<Estados_queja> buscarPorId(String id);

    Optional<Estados_queja> buscarPorNombre(String nombre);

    List<Estados_queja> listar();

    void eliminar(String id);
}
