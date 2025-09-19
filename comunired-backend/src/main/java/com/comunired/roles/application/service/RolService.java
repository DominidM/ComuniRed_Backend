package com.comunired.roles.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.comunired.roles.domain.entity.Rol;
import com.comunired.roles.domain.repository.RolRepository;

@Service
public class RolService {
    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // Método paginado
    public Page<Rol> obtenerRoles(int page, int size) {
        return rolRepository.findAll(PageRequest.of(page, size));
    }

    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    public Rol guardarRol(Rol rol) {
        return rolRepository.save(rol);
    }

    public Rol buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}