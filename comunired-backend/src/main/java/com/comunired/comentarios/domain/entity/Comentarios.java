package com.comunired.comentarios.domain.entity;

import java.time.Instant;

public class Comentarios {
    private String id;
    private String queja_id;
    private String usuario_id;
    private String texto;
    private Instant fecha_creacion;
    private Instant fecha_modificacion;
    
    private Boolean eliminado;
    private String eliminado_por;
    private String razon_eliminacion;
    private Instant fecha_eliminacion;

    private String aprobado_por;
    private Instant fecha_aprobacion;
    
    private Boolean rechazado;
    private String rechazado_por;
    private String razon_rechazo;
    private Instant fecha_rechazo;

    public Comentarios() {
        this.fecha_creacion = Instant.now();
        this.eliminado = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQueja_id() { return queja_id; }
    public void setQueja_id(String queja_id) { this.queja_id = queja_id; }

    public String getUsuario_id() { return usuario_id; }
    public void setUsuario_id(String usuario_id) { this.usuario_id = usuario_id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Instant getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(Instant fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    public Instant getFecha_modificacion() { return fecha_modificacion; }
    public void setFecha_modificacion(Instant fecha_modificacion) { this.fecha_modificacion = fecha_modificacion; }



    public Boolean getEliminado() { return eliminado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }

    public String getEliminado_por() { return eliminado_por; }
    public void setEliminado_por(String eliminado_por) { this.eliminado_por = eliminado_por; }

    public String getRazon_eliminacion() { return razon_eliminacion; }
    public void setRazon_eliminacion(String razon_eliminacion) { this.razon_eliminacion = razon_eliminacion; }

    public Instant getFecha_eliminacion() { return fecha_eliminacion; }
    public void setFecha_eliminacion(Instant fecha_eliminacion) { this.fecha_eliminacion = fecha_eliminacion; }

    public String getAprobado_por() { return aprobado_por; }
    public void setAprobado_por(String aprobado_por) { this.aprobado_por = aprobado_por; }

    public Instant getFecha_aprobacion() { return fecha_aprobacion; }
    public void setFecha_aprobacion(Instant fecha_aprobacion) { this.fecha_aprobacion = fecha_aprobacion; }

    public Boolean getRechazado() { return rechazado; }
    public void setRechazado(Boolean rechazado) { this.rechazado = rechazado; }

    public String getRechazado_por() { return rechazado_por; }
    public void setRechazado_por(String rechazado_por) { this.rechazado_por = rechazado_por; }

    public String getRazon_rechazo() { return razon_rechazo; }
    public void setRazon_rechazo(String razon_rechazo) { this.razon_rechazo = razon_rechazo; }

    public Instant getFecha_rechazo() { return fecha_rechazo; }
    public void setFecha_rechazo(Instant fecha_rechazo) { this.fecha_rechazo = fecha_rechazo; }

}
