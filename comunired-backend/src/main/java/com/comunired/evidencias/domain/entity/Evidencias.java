package com.comunired.evidencias.domain.entity;

import java.time.Instant;

public class Evidencias {
    private String id;
    private String queja_id;
    private String url;
    private String tipo;
    private Instant fecha_subida;
    
    private String subida_por;
    private Boolean es_resolucion;

    public Evidencias() {
        this.fecha_subida = Instant.now();
        this.es_resolucion = false;
    }

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

    public String getSubida_por() { return subida_por; }
    public void setSubida_por(String subida_por) { this.subida_por = subida_por; }

    public Boolean getEs_resolucion() { return es_resolucion; }
    public void setEs_resolucion(Boolean es_resolucion) { this.es_resolucion = es_resolucion; }
}
