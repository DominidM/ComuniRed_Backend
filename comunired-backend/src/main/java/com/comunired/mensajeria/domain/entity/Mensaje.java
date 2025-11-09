package com.comunired.mensajeria.domain.entity;

import java.time.LocalDateTime;

public class Mensaje {
    private String id;
    private String conversacionId;
    private String emisorId;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private Boolean leido;
    private LocalDateTime fechaLectura;

    public Mensaje() {
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }

    public Mensaje(String conversacionId, String emisorId, String contenido) {
        this.conversacionId = conversacionId;
        this.emisorId = emisorId;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }

    // Getters y Setters completos
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
    
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { 
        this.fechaEnvio = fechaEnvio; 
    }
    
    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }
    
    public LocalDateTime getFechaLectura() { return fechaLectura; }
    public void setFechaLectura(LocalDateTime fechaLectura) { 
        this.fechaLectura = fechaLectura; 
    }
}
