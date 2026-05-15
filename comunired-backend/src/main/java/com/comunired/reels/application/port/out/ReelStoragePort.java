package com.comunired.reels.application.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface ReelStoragePort {
    String subirVideo(MultipartFile video);
    void eliminar(String publicId);
}
