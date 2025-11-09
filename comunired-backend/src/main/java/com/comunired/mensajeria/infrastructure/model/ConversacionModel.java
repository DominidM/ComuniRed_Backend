package com.comunired.mensajeria.infrastructure.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "conversaciones")
public class ConversacionModel {
    @Id
    private String id;
    private String participante1Id;
    private String participante2Id;
    private String ultimoMensajeId;
    private Instant fechaCreacion;
    private Instant fechaUltimaActividad;

    public ConversacionModel() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getParticipante1Id() { return participante1Id; }
    public void setParticipante1Id(String participante1Id) { 
        this.participante1Id = participante1Id; 
    }
    
    public String getParticipante2Id() { return participante2Id; }
    public void setParticipante2Id(String participante2Id) { 
        this.participante2Id = participante2Id; 
    }
    
    public String getUltimoMensajeId() { return ultimoMensajeId; }
    public void setUltimoMensajeId(String ultimoMensajeId) { 
        this.ultimoMensajeId = ultimoMensajeId; 
    }
    
    public Instant getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Instant fechaCreacion) { 
        this.fechaCreacion = fechaCreacion; 
    }
    
    public Instant getFechaUltimaActividad() { return fechaUltimaActividad; }
    public void setFechaUltimaActividad(Instant fechaUltimaActividad) { 
        this.fechaUltimaActividad = fechaUltimaActividad; 
    }
}
