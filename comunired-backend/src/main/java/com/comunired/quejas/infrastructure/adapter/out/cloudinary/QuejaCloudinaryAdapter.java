package com.comunired.quejas.infrastructure.adapter.out.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.comunired.quejas.application.port.out.QuejaOutPorts.QuejaImagenPort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class QuejaCloudinaryAdapter implements QuejaImagenPort {

    private final Cloudinary cloudinary;

    public QuejaCloudinaryAdapter(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String subirImagen(MultipartFile imagen) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    imagen.getBytes(),
                    ObjectUtils.asMap("folder", "quejas")
            );
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary", e);
        }
    }

    @Override
    public void eliminarImagen(String imagenUrl) {
        try {
            String publicId = extraerPublicId(imagenUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar imagen de Cloudinary", e);
        }
    }

    private String extraerPublicId(String url) {
        // Ejemplo URL: https://res.cloudinary.com/demo/image/upload/v123/quejas/abc.jpg
        // PublicId: quejas/abc
        String[] parts = url.split("/upload/");
        if (parts.length < 2) throw new IllegalArgumentException("URL de Cloudinary inválida: " + url);
        String path = parts[1].replaceFirst("v\\d+/", "");
        int dotIndex = path.lastIndexOf('.');
        return dotIndex > 0 ? path.substring(0, dotIndex) : path;
    }
}
