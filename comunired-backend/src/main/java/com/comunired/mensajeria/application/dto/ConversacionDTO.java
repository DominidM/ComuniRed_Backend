package com.comunired.mensajeria.application.dto;

import java.time.LocalDateTime;

public class ConversacionDTO {
    private String id;
    private String participante1Id;
    private String participante2Id;
    private String ultimoMensajeId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaActividad;

    public ConversacionDTO() {}

    // Getters y Setters (igual que la entity)
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
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { 
        this.fechaCreacion = fechaCreacion; 
    }
    
    public LocalDateTime getFechaUltimaActividad() { return fechaUltimaActividad; }
    public void setFechaUltimaActividad(LocalDateTime fechaUltimaActividad) { 
        this.fechaUltimaActividad = fechaUltimaActividad; 
    }
}
