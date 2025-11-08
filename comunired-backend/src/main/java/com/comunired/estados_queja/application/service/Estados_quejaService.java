package com.comunired.estados_queja.application.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.comunired.estados_queja.domain.entity.Estados_queja;
import com.comunired.estados_queja.domain.repository.Estados_quejaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
public class Estados_quejaService {

    private final Estados_quejaRepository estadosQuejaRepository;

    public Estados_quejaService(Estados_quejaRepository estadosQuejaRepository) {
        this.estadosQuejaRepository = estadosQuejaRepository;
    }

    public List<Estados_queja> listarEstados() {
        return estadosQuejaRepository.listar();
    }

    public Optional<Estados_queja> buscarPorNombre(String nombre) {
        return estadosQuejaRepository.buscarPorNombre(nombre);
    }

    public Estados_queja crearEstado(String clave, String nombre, String descripcion, int orden) {
        Estados_queja estado = new Estados_queja();
        estado.setClave(clave);
        estado.setNombre(nombre);
        estado.setDescripcion(descripcion);
        estado.setOrden(orden);
        return estadosQuejaRepository.guardar(estado);
    }

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

    public void eliminarEstado(String id) {
        estadosQuejaRepository.eliminar(id);
    }

    public Page<Estados_queja> listarEstadosQuejaPaginado(int page, int size) 
    {
        return estadosQuejaRepository.listarPaginado(PageRequest.of(page, size));
    }

}
