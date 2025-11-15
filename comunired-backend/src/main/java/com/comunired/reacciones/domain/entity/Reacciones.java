package com.comunired.reacciones.domain.entity;

import java.time.Instant;

public class Reacciones {
    private String id;
    private String queja_id;
    private String usuario_id;
    private String tipo_reaccion_id;
    private Instant fecha_reaccion;

    public Reacciones() {
        this.fecha_reaccion = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQueja_id() { return queja_id; }
    public void setQueja_id(String queja_id) { this.queja_id = queja_id; }

    public String getUsuario_id() { return usuario_id; }
    public void setUsuario_id(String usuario_id) { this.usuario_id = usuario_id; }

    public String getTipo_reaccion_id() { return tipo_reaccion_id; }
    public void setTipo_reaccion_id(String tipo_reaccion_id) { this.tipo_reaccion_id = tipo_reaccion_id; }

    public Instant getFecha_reaccion() { return fecha_reaccion; }
    public void setFecha_reaccion(Instant fecha_reaccion) { this.fecha_reaccion = fecha_reaccion; }
}
