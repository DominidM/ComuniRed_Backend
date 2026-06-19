package com.comunired.historias.domain.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Historia {

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
    private Instant fechaExpiracion;
    private List<Vista> vistas;
    private List<Like> likes;
    private List<Respuesta> respuestas;

    public record Vista(String usuarioId, Instant fechaVista) { }
    public record Like(String usuarioId, Instant fechaLike) { }
    public record Respuesta(String usuarioId, String texto, Instant fechaRespuesta) { }

    private Historia() { }

    public static Historia crear(
            String usuarioId, String texto, String imagenUrl, String videoUrl,
            String colorFondo, int duracion, String cancionTitulo, String cancionArtista,
            String cancionPreviewUrl, String cancionCoverUrl
    ) {
        Historia h = new Historia();
        h.usuarioId = usuarioId;
        h.texto = texto;
        h.imagenUrl = imagenUrl;
        h.videoUrl = videoUrl;
        h.colorFondo = colorFondo;
        h.duracion = duracion;
        h.cancionTitulo = cancionTitulo;
        h.cancionArtista = cancionArtista;
        h.cancionPreviewUrl = cancionPreviewUrl;
        h.cancionCoverUrl = cancionCoverUrl;
        h.activa = true;
        h.fechaCreacion = Instant.now();
        h.fechaExpiracion = Instant.now().plusSeconds(86400);
        h.vistas = new ArrayList<>();
        h.likes = new ArrayList<>();
        h.respuestas = new ArrayList<>();
        return h;
    }

    public void marcarVista(String usuarioId) {
        boolean yaVio = vistas.stream()
                .anyMatch(v -> v.usuarioId().equals(usuarioId));
        if (!yaVio) {
            vistas.add(new Vista(usuarioId, Instant.now()));
        }
    }

    public void toggleLike(String usuarioId) {
        var existente = likes.stream()
                .filter(l -> l.usuarioId().equals(usuarioId))
                .findFirst();
        if (existente.isPresent()) {
            likes.remove(existente.get());
        } else {
            likes.add(new Like(usuarioId, Instant.now()));
        }
    }

    public boolean leGusta(String usuarioId) {
        return likes.stream().anyMatch(l -> l.usuarioId().equals(usuarioId));
    }

    public void agregarRespuesta(String usuarioId, String texto) {
        respuestas.add(new Respuesta(usuarioId, texto, Instant.now()));
    }

    public void expirar() { this.activa = false; }

    public boolean fueVistaPor(String usuarioId) {
        return vistas.stream().anyMatch(v -> v.usuarioId().equals(usuarioId));
    }

    public static Historia reconstruir(
            String id, String usuarioId, String texto, String imagenUrl, String videoUrl,
            String colorFondo, int duracion, String cancionTitulo, String cancionArtista,
            String cancionPreviewUrl, String cancionCoverUrl,
            boolean activa, Instant fechaCreacion, Instant fechaExpiracion,
            List<Vista> vistas, List<Like> likes, List<Respuesta> respuestas
    ) {
        Historia h = new Historia();
        h.id = id;
        h.usuarioId = usuarioId;
        h.texto = texto;
        h.imagenUrl = imagenUrl;
        h.videoUrl = videoUrl;
        h.colorFondo = colorFondo;
        h.duracion = duracion;
        h.cancionTitulo = cancionTitulo;
        h.cancionArtista = cancionArtista;
        h.cancionPreviewUrl = cancionPreviewUrl;
        h.cancionCoverUrl = cancionCoverUrl;
        h.activa = activa;
        h.fechaCreacion = fechaCreacion;
        h.fechaExpiracion = fechaExpiracion;
        h.vistas = vistas;
        h.likes = likes;
        h.respuestas = respuestas;
        return h;
    }

    // Getters
    public String getId() { return id; }
    public String getUsuarioId() { return usuarioId; }
    public String getTexto() { return texto; }
    public String getImagenUrl() { return imagenUrl; }
    public String getVideoUrl() { return videoUrl; }
    public String getColorFondo() { return colorFondo; }
    public int getDuracion() { return duracion; }
    public String getCancionTitulo() { return cancionTitulo; }
    public String getCancionArtista() { return cancionArtista; }
    public String getCancionPreviewUrl() { return cancionPreviewUrl; }
    public String getCancionCoverUrl() { return cancionCoverUrl; }
    public boolean isActiva() { return activa; }
    public Instant getFechaCreacion() { return fechaCreacion; }
    public Instant getFechaExpiracion() { return fechaExpiracion; }
    public List<Vista> getVistas() { return vistas; }
    public List<Like> getLikes() { return likes; }
    public List<Respuesta> getRespuestas() { return respuestas; }
    public void setId(String id) { this.id = id; }
}
