package com.comunired.notificaciones.domain.entity;

import java.time.Instant;

public class Notificacion {
    private String id;
    private String usuarioId;
    private String tipo;
    private String titulo;
    private String cuerpo;
    private String referenciaId;
    private Boolean leida;
    private Instant fechaCreacion;
    private Instant fechaLectura;

    public Notificacion(String usuarioId, String tipo, String titulo, String cuerpo, String referenciaId) {
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.referenciaId = referenciaId;
        this.leida = false;
        this.fechaCreacion = Instant.now();
    }

    public Notificacion() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }

    public String getReferenciaId() { return referenciaId; }
    public void setReferenciaId(String referenciaId) { this.referenciaId = referenciaId; }

    public Boolean getLeida() { return leida; }
    public void setLeida(Boolean leida) { this.leida = leida; }

    public Instant getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Instant fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Instant getFechaLectura() { return fechaLectura; }
    public void setFechaLectura(Instant fechaLectura) { this.fechaLectura = fechaLectura; }

    public void marcarLeida() {
        this.leida = true;
        this.fechaLectura = Instant.now();
    }
}
