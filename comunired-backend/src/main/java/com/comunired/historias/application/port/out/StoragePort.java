package com.comunired.historias.application.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface StoragePort {
    String subir(MultipartFile archivo, String carpeta);
    void eliminar(String publicId);
}