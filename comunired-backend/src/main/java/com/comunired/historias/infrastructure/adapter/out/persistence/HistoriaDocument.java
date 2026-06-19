package com.comunired.historias.infrastructure.adapter.out.persistence;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "historias")
public class HistoriaDocument {

    @Id
    private String id;

    private String usuarioId;
    private String texto;
    private String imagenUrl;
    private String videoUrl;
    private String colorFondo;
    private int duracion;
    private String cancionTitulo;
    private String cancionArtista;
    private String cancionPreviewUrl;
    private String cancionCoverUrl;
    private boolean activa;
    private Instant fechaCreacion;

    @Indexed(expireAfterSeconds = 0)
    private Instant fechaExpiracion;  // TTL index — MongoDB borra automático

    private List<VistaDocument> vistas = new ArrayList<>();
    private List<LikeDocument> likes = new ArrayList<>();
    private List<RespuestaDocument> respuestas = new ArrayList<>();

    @Data
    public static class VistaDocument {
        private String usuarioId;
        private Instant fechaVista;
    }

    @Data
    public static class LikeDocument {
        private String usuarioId;
        private Instant fechaLike;
    }

    @Data
    public static class RespuestaDocument {
        private String usuarioId;
        private String texto;
        private Instant fechaRespuesta;
    }
}