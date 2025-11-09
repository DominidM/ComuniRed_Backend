package com.comunired.mensajeria.infrastructure.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "mensajes")
public class MensajeModel {
    @Id
    private String id;
    private String conversacionId;
    private String emisorId;
    private String contenido;
    private Instant fechaEnvio;
    private Boolean leido;
    private Instant fechaLectura;

    public MensajeModel() {}

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
