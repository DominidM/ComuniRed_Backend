package com.comunired.estados_queja.application.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.comunired.estados_queja.domain.entity.Estados_queja;
import com.comunired.estados_queja.domain.repository.Estados_quejaRepository;

@Service
public class Estados_quejaService {

    private final Estados_quejaRepository estadosQuejaRepository;

    public Estados_quejaService(Estados_quejaRepository estadosQuejaRepository) {
        this.estadosQuejaRepository = estadosQuejaRepository;
    }

    // Listar todos los estados
    public List<Estados_queja> listarEstados() {
        return estadosQuejaRepository.listar();
    }

    // Buscar por nombre
    public Optional<Estados_queja> buscarPorNombre(String nombre) {
        return estadosQuejaRepository.buscarPorNombre(nombre);
    }

    // Crear nuevo estado
    public Estados_queja crearEstado(String clave, String nombre, String descripcion, int orden) {
        Estados_queja estado = new Estados_queja();
        estado.setClave(clave);
        estado.setNombre(nombre);
        estado.setDescripcion(descripcion);
        estado.setOrden(orden);
        return estadosQuejaRepository.guardar(estado);
    }

    // Actualizar estado existente
    public Estados_queja actualizarEstado(String id, String clave, String nombre, String descripcion, int orden) {
        Optional<Estados_queja> estadoOpt = estadosQuejaRepository.buscarPorId(id);
        if (estadoOpt.isPresent()) {
            Estados_queja estado = estadoOpt.get();
            estado.setClave(clave);
            estado.setNombre(nombre);
            estado.setDescripcion(descripcion);
            estado.setOrden(orden);
            return estadosQuejaRepository.modificar(estado);
        } else {
            throw new RuntimeException("Estado no encontrado con id: " + id);
        }
    }

    // Eliminar por id
    public void eliminarEstado(String id) {
        estadosQuejaRepository.eliminar(id);
    }
}
