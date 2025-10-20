package com.comunired.roles.application.dto;

/**
 * DTO simple para roles (puedes ampliarlo si lo necesitas).
 */
public class RolDTO {
    private String id;
    private String nombre;
    private String descripcion;

    public RolDTO() {}

    public RolDTO(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}