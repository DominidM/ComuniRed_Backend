package com.comunired.historias.infrastructure.adapter.in.rest;

import com.comunired.historias.application.dto.out.MusicTrackResponse;
import com.comunired.historias.infrastructure.adapter.out.musica.MusicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/musica")
public class MusicController {

    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<MusicTrackResponse>> buscar(@RequestParam String q) {
        if (q == null || q.isBlank()) {
            return ResponseEntity.badRequest().body(List.of());
        }
        return ResponseEntity.ok(musicService.buscar(q));
    }
}
