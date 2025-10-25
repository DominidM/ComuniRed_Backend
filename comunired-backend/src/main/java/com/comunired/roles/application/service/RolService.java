package com.comunired.roles.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.comunired.roles.application.dto.RolDTO;
import com.comunired.roles.application.port.out.RolRepository;

@Service
public class RolService {
    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public Page<RolDTO> obtenerRoles(int page, int size) {
        return rolRepository.findAll(PageRequest.of(page, size));
    }

    public List<RolDTO> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    public RolDTO guardarRol(RolDTO rol) {
        return rolRepository.save(rol);
    }

    public RolDTO  buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    public RolDTO buscarPorId(String id) {
        Optional<RolDTO> optional = rolRepository.findById(id);
        return optional.orElse(null);
    }

    // Eliminación robusta: solo elimina si existe, retorna true si elimina, false si no existe
    public boolean eliminarPorId(String id) {
        if (!rolRepository.existsById(id)) {
            System.out.println("No existe el rol con id: " + id);
            return false;
        }
        rolRepository.deleteById(id);
        System.out.println("Rol eliminado con id: " + id);
        return true;
    }
}