package com.comunired.reels.domain.entity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Reel {

    private String id;
    private String videoUrl;
    private String title;
    private String description;
    private String authorId;
    private String author;
    private String avatarUrl;
    private int likes;
    private int shares;
    private int vistas;
    private int comentariosCount;
    private boolean activo;
    private Instant fechaCreacion;
    private Instant fechaExpiracion;
    private List<String> likedBy;
    private List<String> savedBy;

    public static Reel crear(String videoUrl, String title, String description,
                             String authorId, String author, String avatarUrl) {
        Reel r = new Reel();
        r.videoUrl = videoUrl;
        r.title = title;
        r.description = description;
        r.authorId = authorId;
        r.author = author;
        r.avatarUrl = avatarUrl;
        r.likes = 0;
        r.shares = 0;
        r.vistas = 0;
        r.comentariosCount = 0;
        r.activo = true;
        r.fechaCreacion = Instant.now();
        r.fechaExpiracion = Instant.now().plus(7, ChronoUnit.DAYS);
        r.likedBy = new ArrayList<>();
        r.savedBy = new ArrayList<>();
        return r;
    }

    public static Reel reconstruir(String id, String videoUrl, String title, String description,
                                   String authorId, String author, String avatarUrl,
                                   int likes, int shares, int vistas, int comentariosCount,
                                   boolean activo, Instant fechaCreacion, Instant fechaExpiracion,
                                   List<String> likedBy, List<String> savedBy) {
        Reel r = new Reel();
        r.id = id;
        r.videoUrl = videoUrl;
        r.title = title;
        r.description = description;
        r.authorId = authorId;
        r.author = author;
        r.avatarUrl = avatarUrl;
        r.likes = likes;
        r.shares = shares;
        r.vistas = vistas;
        r.comentariosCount = comentariosCount;
        r.activo = activo;
        r.fechaCreacion = fechaCreacion;
        r.fechaExpiracion = fechaExpiracion;
        r.likedBy = likedBy != null ? likedBy : new ArrayList<>();
        r.savedBy = savedBy != null ? savedBy : new ArrayList<>();
        return r;
    }

    public boolean toggleLike(String usuarioId) {
        if (likedBy.contains(usuarioId)) {
            likedBy.remove(usuarioId);
            likes = Math.max(0, likes - 1);
            return false; // liked=false
        } else {
            likedBy.add(usuarioId);
            likes++;
            return true; // liked=true
        }
    }

    public boolean toggleSave(String usuarioId) {
        if (savedBy.contains(usuarioId)) {
            savedBy.remove(usuarioId);
            return false;
        } else {
            savedBy.add(usuarioId);
            return true;
        }
    }

    public void incrementarVista() {
        this.vistas++;
    }

    public boolean fueLikedPor(String usuarioId) {
        return likedBy.contains(usuarioId);
    }

    public boolean fueSavedPor(String usuarioId) {
        return savedBy.contains(usuarioId);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
    public int getShares() { return shares; }
    public void setShares(int shares) { this.shares = shares; }
    public int getVistas() { return vistas; }
    public void setVistas(int vistas) { this.vistas = vistas; }
    public int getComentariosCount() { return comentariosCount; }
    public void setComentariosCount(int comentariosCount) { this.comentariosCount = comentariosCount; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public Instant getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Instant fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Instant getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Instant fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public List<String> getLikedBy() { return likedBy; }
    public void setLikedBy(List<String> likedBy) { this.likedBy = likedBy; }
    public List<String> getSavedBy() { return savedBy; }
    public void setSavedBy(List<String> savedBy) { this.savedBy = savedBy; }
}
