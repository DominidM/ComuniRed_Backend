package com.comunired.tipos_reaccion.domain.entity;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "tipos_reaccion")
public class Tipos_reaccion implements Serializable {

    @Id
    private String id;
    private String key;
    private String label;
    private boolean activo;
    private int orden;

    public Tipos_reaccion() {}

    public Tipos_reaccion(String id, String key, String label, boolean activo, int orden) {
        this.id = id;
        this.key = key;
        this.label = label;
        this.activo = activo;
        this.orden = orden;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getOrden() {
        return orden;
    }
    public void setOrden(int orden) {
        this.orden = orden;
    }
}
