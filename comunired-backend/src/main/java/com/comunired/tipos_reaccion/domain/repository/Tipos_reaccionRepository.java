package com.comunired.tipos_reaccion.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion;

public interface Tipos_reaccionRepository {

    Tipos_reaccion guardar(Tipos_reaccion tipoReaccion);

    Tipos_reaccion modificar(Tipos_reaccion tipoReaccion);

    Optional<Tipos_reaccion> buscarPorId(String id);

    Optional<Tipos_reaccion> buscarPorLabel(String label);

    List<Tipos_reaccion> listar();

    void eliminar(String id);

    Page<Tipos_reaccion> listarPaginado(Pageable pageable);

}
