package com.comunired.usuarios.infrastructure.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Sube imagen a Cloudinary y devuelve URL
     */
    public String subirImagen(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        logger.info("📤 Subiendo imagen a Cloudinary: {} ({} bytes)", 
            file.getOriginalFilename(), file.getSize());
        
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder", "usuarios",
                    "resource_type", "image",
                    "transformation", ObjectUtils.asMap(
                        "width", 400,
                        "height", 400,
                        "crop", "fill",
                        "quality", "auto"
                    )
                )
            );

            String url = (String) uploadResult.get("secure_url");
            logger.info("✅ Imagen subida: {}", url);
            return url;
            
        } catch (IOException e) {
            logger.error("❌ Error subiendo imagen: {}", e.getMessage());
            throw new IOException("Error al subir imagen a Cloudinary: " + e.getMessage());
        }
    }

    /**
     * Sube desde Base64 (para migración)
     */
    public String subirDesdeBase64(String base64String) throws IOException {
        if (base64String == null || base64String.isEmpty()) {
            throw new IllegalArgumentException("El string Base64 no puede estar vacío");
        }

        logger.info("📤 Subiendo imagen Base64 a Cloudinary");
        
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
            base64String,
            ObjectUtils.asMap(
                "folder", "usuarios",
                "resource_type", "image"
            )
        );

        String url = (String) uploadResult.get("secure_url");
        logger.info("✅ Imagen Base64 subida: {}", url);
        return url;
    }

    /**
     * Elimina imagen de Cloudinary
     * @return true si se eliminó, false si hubo error
     */
    public boolean eliminarImagen(String url) {  // ← CAMBIAR: void → boolean
        if (url == null || !url.contains("cloudinary.com")) {
            logger.warn("⚠️ URL no válida para eliminar: {}", url);
            return false;
        }

        try {
            String publicId = extraerPublicId(url);
            if (publicId == null) {
                logger.warn("⚠️ No se pudo extraer public_id de: {}", url);
                return false;
            }

            logger.info("🗑️ Eliminando imagen: {}", publicId);
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            
            String resultStatus = (String) result.get("result");
            boolean success = "ok".equals(resultStatus);
            
            if (success) {
                logger.info("✅ Imagen eliminada exitosamente");
            } else {
                logger.warn("⚠️ Cloudinary respondió: {}", resultStatus);
            }
            
            return success;
            
        } catch (IOException e) {
            logger.error("❌ Error eliminando imagen: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrae el public_id de una URL de Cloudinary
     * Ejemplo: https://res.cloudinary.com/demo/image/upload/v1234/usuarios/abc123.jpg
     * Public ID: usuarios/abc123
     */
    public String extraerPublicId(String url) {
        if (url == null || !url.contains("cloudinary.com")) {
            return null;
        }
        
        try {
            String[] parts = url.split("/upload/");
            if (parts.length < 2) return null;
            
            String afterUpload = parts[1];
            String[] versionSplit = afterUpload.split("/", 2);
            if (versionSplit.length < 2) return null;
            
            String pathWithExtension = versionSplit[1];
            String publicId = pathWithExtension.replaceFirst("\\.[^.]+$", "");
            
            logger.debug("📋 Public ID extraído: {}", publicId);
            return publicId;
            
        } catch (Exception e) {
            logger.error("❌ Error extrayendo public_id: {}", e.getMessage());
            return null;
        }
    }
}
