package com.comunired.reels.application.dto.in;

import org.springframework.web.multipart.MultipartFile;

public record ActualizarReelCommand(
    MultipartFile video,
    String title,
    String description,
    String author,
    String avatarUrl
) {}
