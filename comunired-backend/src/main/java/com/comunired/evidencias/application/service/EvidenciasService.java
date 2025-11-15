package com.comunired.evidencias.application.service;

import com.comunired.evidencias.domain.entity.Evidencias;
import com.comunired.evidencias.domain.repository.EvidenciasRepository;
import com.comunired.evidencias.application.dto.EvidenciasDTO;
import com.comunired.usuarios.infrastructure.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvidenciasService {

    @Autowired
    private EvidenciasRepository evidenciasRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public EvidenciasDTO uploadEvidencia(String quejaId, MultipartFile archivo, String tipo) {
        try {
            String url = cloudinaryService.subirImagen(archivo);

            Evidencias evidencia = new Evidencias();
            evidencia.setQueja_id(quejaId);
            evidencia.setUrl(url);
            evidencia.setTipo(tipo != null ? tipo : detectarTipo(archivo));

            Evidencias saved = evidenciasRepository.save(evidencia);
            return toDTO(saved);
        } catch (Exception e) {
            throw new RuntimeException("Error al subir evidencia: " + e.getMessage());
        }
    }

    public List<EvidenciasDTO> findByQuejaId(String quejaId) {
        return evidenciasRepository.findByQuejaId(quejaId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public boolean delete(String id) {
        if (!evidenciasRepository.existsById(id)) {
            return false;
        }
        evidenciasRepository.deleteById(id);
        return true;
    }

    private String detectarTipo(MultipartFile archivo) {
        String contentType = archivo.getContentType();
        if (contentType == null) return "DOCUMENT";
        
        if (contentType.startsWith("image/")) return "IMAGE";
        if (contentType.startsWith("video/")) return "VIDEO";
        return "DOCUMENT";
    }

    private EvidenciasDTO toDTO(Evidencias evidencia) {
        EvidenciasDTO dto = new EvidenciasDTO();
        dto.setId(evidencia.getId());
        dto.setQueja_id(evidencia.getQueja_id());
        dto.setUrl(evidencia.getUrl());
        dto.setTipo(evidencia.getTipo());
        dto.setFecha_subida(evidencia.getFecha_subida());
        return dto;
    }
}
