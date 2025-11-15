package com.comunired.evidencias.application.dto;

import java.time.Instant;

public class EvidenciasDTO {
    private String id;
    private String queja_id;
    private String url;
    private String tipo;
    private Instant fecha_subida;

    public EvidenciasDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQueja_id() { return queja_id; }
    public void setQueja_id(String queja_id) { this.queja_id = queja_id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Instant getFecha_subida() { return fecha_subida; }
    public void setFecha_subida(Instant fecha_subida) { this.fecha_subida = fecha_subida; }
}
