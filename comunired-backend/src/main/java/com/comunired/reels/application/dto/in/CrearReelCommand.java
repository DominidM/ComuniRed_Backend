package com.comunired.reels.application.dto.in;

import org.springframework.web.multipart.MultipartFile;

public record CrearReelCommand(
    MultipartFile video,
    String title,
    String description,
    String authorId,
    String author,
    String avatarUrl
) {}
