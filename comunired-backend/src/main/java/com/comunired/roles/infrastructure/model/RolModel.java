package com.comunired.roles.infrastructure.model;

public class RolModel {
    private String id;
    private String nombre;
    private String descripcion; // <--- Agregado

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}