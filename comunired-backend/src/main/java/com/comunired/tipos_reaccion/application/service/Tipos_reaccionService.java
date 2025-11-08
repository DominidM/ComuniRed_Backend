package com.comunired.tipos_reaccion.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion;
import com.comunired.tipos_reaccion.domain.repository.Tipos_reaccionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@Service
public class Tipos_reaccionService {

    private final Tipos_reaccionRepository tiposReaccionRepository;

    public Tipos_reaccionService(Tipos_reaccionRepository tiposReaccionRepository) {
        this.tiposReaccionRepository = tiposReaccionRepository;
    }

    public Tipos_reaccion crearTipoReaccion(String key, String label, boolean activo, int orden) {
        Tipos_reaccion tipo = new Tipos_reaccion(null, key, label, activo, orden);
        return tiposReaccionRepository.guardar(tipo);
    }

    public Tipos_reaccion actualizarTipoReaccion(String id, String key, String label, boolean activo, int orden) {
        Optional<Tipos_reaccion> existente = tiposReaccionRepository.buscarPorId(id);
        if (existente.isPresent()) {
            Tipos_reaccion tipo = existente.get();
            tipo.setKey(key);
            tipo.setLabel(label);
            tipo.setActivo(activo);
            tipo.setOrden(orden);
            return tiposReaccionRepository.modificar(tipo);
        }
        return null;
    }

    public Optional<Tipos_reaccion> buscarPorId(String id) {
        return tiposReaccionRepository.buscarPorId(id);
    }

    public Optional<Tipos_reaccion> buscarPorLabel(String label) {
        return tiposReaccionRepository.buscarPorLabel(label);
    }

    public List<Tipos_reaccion> listarTiposReaccion() {
        return tiposReaccionRepository.listar();
    }

    public void eliminarTipoReaccion(String id) {
        tiposReaccionRepository.eliminar(id);
    }

    public Page<Tipos_reaccion> listarTiposReaccionPaginado(int page, int size) {
        return tiposReaccionRepository.listarPaginado(PageRequest.of(page, size));
    }

}
