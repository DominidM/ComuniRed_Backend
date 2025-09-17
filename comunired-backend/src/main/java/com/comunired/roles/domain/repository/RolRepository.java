package com.comunired.roles.domain.repository;

import java.util.List;

import com.comunired.roles.domain.entity.Rol;

public interface RolRepository {
    List<Rol> findAll();
    Rol save(Rol rol);
    Rol findByNombre(String nombre);
}