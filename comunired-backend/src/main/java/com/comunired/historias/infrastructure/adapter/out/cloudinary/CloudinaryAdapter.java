package com.comunired.historias.infrastructure.adapter.out.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.comunired.historias.application.port.out.StoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryAdapter implements StoragePort {

    private final Cloudinary cloudinary;

    @Override
    public String subir(MultipartFile archivo, String carpeta) {
        try {
            Map result = cloudinary.uploader().upload(
                archivo.getBytes(),
                ObjectUtils.asMap(
                    "folder", "comunired/" + carpeta,
                    "resource_type", "auto"
                )
            );
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir archivo a Cloudinary", e);
        }
    }

    @Override
    public void eliminar(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar archivo de Cloudinary", e);
        }
    }
}