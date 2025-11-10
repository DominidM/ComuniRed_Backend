package com.comunired.seguimientos.application.dto;

public class EstadoRelacionDTO {
    private Boolean estaSiguiendo;
    private Boolean teSigue;
    private Boolean seguimientoMutuo;
    private Boolean solicitudPendiente;
    private Boolean solicitudEnviada;

    public EstadoRelacionDTO() {}


    public Boolean getEstaSiguiendo() { return estaSiguiendo; }
    public void setEstaSiguiendo(Boolean estaSiguiendo) { 
        this.estaSiguiendo = estaSiguiendo; 
    }
    
    public Boolean getTeSigue() { return teSigue; }
    public void setTeSigue(Boolean teSigue) { this.teSigue = teSigue; }
    
    public Boolean getSeguimientoMutuo() { return seguimientoMutuo; }
    public void setSeguimientoMutuo(Boolean seguimientoMutuo) { 
        this.seguimientoMutuo = seguimientoMutuo; 
    }
    
    public Boolean getSolicitudPendiente() { return solicitudPendiente; }
    public void setSolicitudPendiente(Boolean solicitudPendiente) { 
        this.solicitudPendiente = solicitudPendiente; 
    }
    
    public Boolean getSolicitudEnviada() { return solicitudEnviada; }
    public void setSolicitudEnviada(Boolean solicitudEnviada) { 
        this.solicitudEnviada = solicitudEnviada; 
    }
}
