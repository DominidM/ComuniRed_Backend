package com.comunired.roles.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.comunired.roles.domain.entity.Rol;
import com.comunired.roles.domain.repository.RolRepository;
import com.comunired.roles.infrastructure.repository.RolMongoRepository;

@Service
public class RolService {
    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

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

    public Rol buscarPorId(String id) {
        if (rolRepository instanceof RolMongoRepository) {
            Optional<Rol> optional = ((RolMongoRepository) rolRepository).findById(id);
            return optional.orElse(null);
        }
        throw new UnsupportedOperationException("Método buscarPorId no implementado para este repositorio");
    }

    // Eliminación robusta: solo elimina si existe, retorna true si elimina, false si no existe
    public boolean eliminarPorId(String id) {
        if (rolRepository instanceof RolMongoRepository) {
            RolMongoRepository mongoRepo = (RolMongoRepository) rolRepository;
            if (!mongoRepo.existsById(id)) {
                System.out.println("No existe el rol con id: " + id);
                return false;
            }
            mongoRepo.deleteById(id);
            System.out.println("Rol eliminado con id: " + id);
            return true;
        }
        throw new UnsupportedOperationException("Método eliminarPorId no implementado para este repositorio");
    }
}