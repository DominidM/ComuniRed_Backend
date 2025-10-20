package com.comunired.roles.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.comunired.roles.domain.entity.Rol;

/**
 * Interfaz de repositorio usada por la capa de servicio.
 * Implementa métodos necesarios y delega internamente a un RolMongoRepository.
 */
public interface RolRepository {

    List<Rol> findAll();

    Page<Rol> findAll(Pageable pageable);

    Rol save(Rol rol);

    Rol findByNombre(String nombre);

    Optional<Rol> findById(String id);

    boolean existsById(String id);

    void deleteById(String id);
}