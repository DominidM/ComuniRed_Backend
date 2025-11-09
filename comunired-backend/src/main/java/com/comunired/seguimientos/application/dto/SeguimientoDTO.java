package com.comunired.seguimientos.application.dto;

import java.time.Instant;

public class SeguimientoDTO {
    private String id;
    private String seguidorId;
    private String seguidoId;
    private String estado;  // String para GraphQL
    private Instant fechaSeguimiento;
    private Instant fechaRespuesta;
    private Boolean notificacionesActivas;

    public SeguimientoDTO() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getSeguidorId() { return seguidorId; }
    public void setSeguidorId(String seguidorId) { this.seguidorId = seguidorId; }
    
    public String getSeguidoId() { return seguidoId; }
    public void setSeguidoId(String seguidoId) { this.seguidoId = seguidoId; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public Instant getFechaSeguimiento() { return fechaSeguimiento; }
    public void setFechaSeguimiento(Instant fechaSeguimiento) { 
        this.fechaSeguimiento = fechaSeguimiento; 
    }
    
    public Instant getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(Instant fechaRespuesta) { 
        this.fechaRespuesta = fechaRespuesta; 
    }
    
    public Boolean getNotificacionesActivas() { return notificacionesActivas; }
    public void setNotificacionesActivas(Boolean notificacionesActivas) { 
        this.notificacionesActivas = notificacionesActivas; 
    }
}
