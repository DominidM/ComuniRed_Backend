package com.comunired.mensajeria.domain.entity;

import java.time.Instant;  // ← IMPORTANTE: Instant

public class Mensaje {
    private String id;
    private String conversacionId;
    private String emisorId;
    private String contenido;
    private Instant fechaEnvio;       // ← Instant
    private Boolean leido;
    private Instant fechaLectura;     // ← Instant

    public Mensaje() {
        this.fechaEnvio = Instant.now();
        this.leido = false;
    }

    public Mensaje(String conversacionId, String emisorId, String contenido) {
        this.conversacionId = conversacionId;
        this.emisorId = emisorId;
        this.contenido = contenido;
        this.fechaEnvio = Instant.now();
        this.leido = false;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getConversacionId() { return conversacionId; }
    public void setConversacionId(String conversacionId) { 
        this.conversacionId = conversacionId; 
    }
    
    public String getEmisorId() { return emisorId; }
    public void setEmisorId(String emisorId) { this.emisorId = emisorId; }
    
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    
    public Instant getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Instant fechaEnvio) { 
        this.fechaEnvio = fechaEnvio; 
    }
    
    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }
    
    public Instant getFechaLectura() { return fechaLectura; }
    public void setFechaLectura(Instant fechaLectura) { 
        this.fechaLectura = fechaLectura; 
    }
}
