package com.comunired.reels.infrastructure.adapter.out.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.comunired.reels.application.port.out.ReelStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReelCloudinaryAdapter implements ReelStoragePort {

    private final Cloudinary cloudinary;

    @Override
    public String subirVideo(MultipartFile video) {
        try {
            Map result = cloudinary.uploader().upload(
                video.getBytes(),
                ObjectUtils.asMap(
                    "folder", "comunired/reels",
                    "resource_type", "video"
                )
            );
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir video a Cloudinary", e);
        }
    }

    @Override
    public void eliminar(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar video de Cloudinary", e);
        }
    }
}
