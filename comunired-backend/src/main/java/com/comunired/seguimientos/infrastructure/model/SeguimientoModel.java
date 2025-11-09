package com.comunired.seguimientos.infrastructure.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import java.time.Instant;

@Document(collection = "seguimientos")
@CompoundIndex(name = "seguidor_seguido_unique", 
               def = "{'seguidorId': 1, 'seguidoId': 1}", 
               unique = true)
public class SeguimientoModel {
    @Id
    private String id;
    private String seguidorId;
    private String seguidoId;
    private String estado;  // PENDIENTE, ACEPTADO, RECHAZADO
    private Instant fechaSeguimiento;
    private Instant fechaRespuesta;
    private Boolean notificacionesActivas;

    public SeguimientoModel() {}

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
        this.notificacionesActivas = notificacionesActivas; }
}
