package com.comunired.reels.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "reel_comentarios")
public class ReelComentarioDocument {

    @Id
    private String id;
    private String reelId;
    private String usuarioId;
    private String usuarioNombre;
    private String usuarioAvatar;
    private String texto;
    private Instant fechaCreacion;

    public ReelComentarioDocument() {}

    public ReelComentarioDocument(String reelId, String usuarioId, String usuarioNombre, String usuarioAvatar, String texto) {
        this.reelId = reelId;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.usuarioAvatar = usuarioAvatar;
        this.texto = texto;
        this.fechaCreacion = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getReelId() { return reelId; }
    public void setReelId(String reelId) { this.reelId = reelId; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
    public String getUsuarioAvatar() { return usuarioAvatar; }
    public void setUsuarioAvatar(String usuarioAvatar) { this.usuarioAvatar = usuarioAvatar; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public Instant getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Instant fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
