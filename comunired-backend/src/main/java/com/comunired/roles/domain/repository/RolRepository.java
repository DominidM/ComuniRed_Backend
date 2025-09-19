package com.comunired.roles.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.comunired.roles.domain.entity.Rol;

public interface RolRepository {
    List<Rol> findAll();
    Page<Rol> findAll(Pageable pageable);
    Rol save(Rol rol);
    Rol findByNombre(String nombre);
}