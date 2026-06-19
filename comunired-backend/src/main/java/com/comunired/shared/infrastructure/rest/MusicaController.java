package com.comunired.shared.infrastructure.rest;

import com.comunired.historias.application.dto.out.MusicTrackResponse;
import com.comunired.historias.infrastructure.adapter.out.musica.MusicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/musica")
public class MusicaController {

    private final MusicService musicService;

    public MusicaController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<MusicTrackResponse>> search(@RequestParam String q) {
        if (q == null || q.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(musicService.buscar(q));
    }
}
