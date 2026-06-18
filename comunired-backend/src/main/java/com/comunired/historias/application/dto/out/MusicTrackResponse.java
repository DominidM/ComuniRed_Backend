package com.comunired.historias.application.dto.out;

public record MusicTrackResponse(
    String id,
    String title,
    String artist,
    String coverUrl,
    String previewUrl
) {}
