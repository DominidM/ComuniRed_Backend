package com.comunired.evidencias.infrastructure.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "evidencias")
public class EvidenciasModel {
    @Id
    private String id;
    private String queja_id;
    private String url;
    private String tipo;
    private Instant fecha_subida;
    
    private String subida_por;
    private Boolean es_resolucion;

    public EvidenciasModel() {}

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
