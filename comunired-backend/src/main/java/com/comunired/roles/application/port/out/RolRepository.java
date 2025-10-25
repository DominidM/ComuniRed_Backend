package com.comunired.roles.application.port.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.comunired.roles.application.dto.RolDTO;

/**
 * Port de salida para la capa de aplicación: expone operaciones orientadas a DTO/paginación.
 * Implementado en infrastructure (adapter).
 */
public interface RolRepository {

    List<RolDTO> findAll();

    Page<RolDTO> findAll(Pageable pageable);

    RolDTO save(RolDTO rol);

    RolDTO findByNombre(String nombre);

    Optional<RolDTO> findById(String id);

    boolean existsById(String id);

    void deleteById(String id);
}