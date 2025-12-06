package com.comunired.historial_evento.domain.entity;

import java.time.Instant;

public class HistorialEvento {
    private String id;
    private String queja_id;
    private String usuario_id;
    private String tipo_evento;
    private String estado_anterior;
    private String estado_nuevo;
    private String descripcion;
    private Instant fecha_evento;

    public HistorialEvento() {
        this.fecha_evento = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQueja_id() { return queja_id; }
    public void setQueja_id(String queja_id) { this.queja_id = queja_id; }

    public String getUsuario_id() { return usuario_id; }
    public void setUsuario_id(String usuario_id) { this.usuario_id = usuario_id; }

    public String getTipo_evento() { return tipo_evento; }
    public void setTipo_evento(String tipo_evento) { this.tipo_evento = tipo_evento; }

    public String getEstado_anterior() { return estado_anterior; }
    public void setEstado_anterior(String estado_anterior) { this.estado_anterior = estado_anterior; }

    public String getEstado_nuevo() { return estado_nuevo; }
    public void setEstado_nuevo(String estado_nuevo) { this.estado_nuevo = estado_nuevo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Instant getFecha_evento() { return fecha_evento; }
    public void setFecha_evento(Instant fecha_evento) { this.fecha_evento = fecha_evento; }
}
