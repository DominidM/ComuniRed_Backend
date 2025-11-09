package com.comunired.mensajeria.application.dto;

import java.time.LocalDateTime;

public class MensajeDTO {
    private String id;
    private String conversacionId;
    private String emisorId;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private Boolean leido;
    private LocalDateTime fechaLectura;

    public MensajeDTO() {}

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
