package com.comunired.estados_queja.domain.entity;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "estados_queja")
public class Estados_queja implements Serializable {

    @Id
    private String id;
    private String clave;
    private String nombre;
    private String descripcion;
    private int orden;

    public Estados_queja() {
    }

    public Estados_queja(String id, String clave, String nombre, String descripcion, int orden) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.orden = orden;
    }

    public String getId() {
        return id;
    }

    public String getClave() {
        return clave;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getOrden() {
        return orden;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
