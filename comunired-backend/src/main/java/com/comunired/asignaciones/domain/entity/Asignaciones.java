package com.comunired.asignaciones.domain.entity;

import java.time.Instant;

public class Asignaciones {
    private String id;
    private String queja_id;
    private String soporte_id;
    private String asignado_por_id;
    private String estado;
    private Instant fecha_asignacion;
    private Instant fecha_actualizacion;
    private String observacion;

    public Asignaciones() {
        this.fecha_asignacion = Instant.now();
        this.estado = "PENDIENTE";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQueja_id() { return queja_id; }
    public void setQueja_id(String queja_id) { this.queja_id = queja_id; }

    public String getSoporte_id() { return soporte_id; }
    public void setSoporte_id(String soporte_id) { this.soporte_id = soporte_id; }

    public String getAsignado_por_id() { return asignado_por_id; }
    public void setAsignado_por_id(String asignado_por_id) { this.asignado_por_id = asignado_por_id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Instant getFecha_asignacion() { return fecha_asignacion; }
    public void setFecha_asignacion(Instant fecha_asignacion) { this.fecha_asignacion = fecha_asignacion; }

    public Instant getFecha_actualizacion() { return fecha_actualizacion; }
    public void setFecha_actualizacion(Instant fecha_actualizacion) { this.fecha_actualizacion = fecha_actualizacion; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
