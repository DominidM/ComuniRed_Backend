package com.comunired.historias.domain.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Historia {

    private String id;
    private String usuarioId;
    private String texto;
    private String imagenUrl;
    private String colorFondo;
    private int duracion;
    private boolean activa;
    private Instant fechaCreacion;
    private Instant fechaExpiracion;
    private List<Vista> vistas;

    public record Vista(String usuarioId, Instant fechaVista) {

    }

    private Historia() {
    }

    public static Historia crear(
            String usuarioId,
            String texto,
            String imagenUrl,
            String colorFondo,
            int duracion
    ) {
        Historia h = new Historia();
        h.usuarioId = usuarioId;
        h.texto = texto;
        h.imagenUrl = imagenUrl;
        h.colorFondo = colorFondo;
        h.duracion = duracion;
        h.activa = true;
        h.fechaCreacion = Instant.now();
        h.fechaExpiracion = Instant.now().plusSeconds(86400); // 24h
        h.vistas = new ArrayList<>();
        return h;
    }

    public void marcarVista(String usuarioId) {
        boolean yaVio = vistas.stream()
                .anyMatch(v -> v.usuarioId().equals(usuarioId));
        if (!yaVio) {
            vistas.add(new Vista(usuarioId, Instant.now()));
        }
    }

    public void expirar() {
        this.activa = false;
    }

    public boolean fueVistaPor(String usuarioId) {
        return vistas.stream()
                .anyMatch(v -> v.usuarioId().equals(usuarioId));
    }

    public static Historia reconstruir(
            String id,
            String usuarioId,
            String texto,
            String imagenUrl,
            String colorFondo,
            int duracion,
            boolean activa,
            Instant fechaCreacion,
            Instant fechaExpiracion,
            List<Vista> vistas
    ) {
        Historia h = new Historia();
        h.id = id;
        h.usuarioId = usuarioId;
        h.texto = texto;
        h.imagenUrl = imagenUrl;
        h.colorFondo = colorFondo;
        h.duracion = duracion;
        h.activa = activa;
        h.fechaCreacion = fechaCreacion;
        h.fechaExpiracion = fechaExpiracion;
        h.vistas = vistas;
        return h;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public String getTexto() {
        return texto;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public String getColorFondo() {
        return colorFondo;
    }

    public int getDuracion() {
        return duracion;
    }

    public boolean isActiva() {
        return activa;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public Instant getFechaExpiracion() {
        return fechaExpiracion;
    }

    public List<Vista> getVistas() {
        return vistas;
    }

    public void setId(String id) {
        this.id = id;
    }
}
