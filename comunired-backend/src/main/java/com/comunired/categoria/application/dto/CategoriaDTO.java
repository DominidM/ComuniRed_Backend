package com.comunired.categoria.application.dto;

public class CategoriaDTO {

    private String id;
    private String nombre;
    private String descripcion;
    private boolean activo;

    public CategoriaDTO(boolean activo, String descripcion, String id, String nombre) {
        this.activo = activo;
        this.descripcion = descripcion;
        this.id = id;
        this.nombre = nombre;
    }

    public CategoriaDTO() {
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
