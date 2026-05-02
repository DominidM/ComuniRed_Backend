package com.comunired.quejas.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "quejas")
public class QuejaDocument {

    @Id
    private String id;
    private String titulo;
    private String descripcion;
    private String usuarioId;
    private String categoriaId;
    private String estadoId;
    private String ubicacion;
    private String imagenUrl;
    private Instant fechaCreacion;
    private Instant fechaActualizacion;
    private String nivelRiesgo;
    private Instant fechaClasificacion;
    private String clasificadoPorId;
    private Instant fechaAprobacion;

    // Getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getUsuarioId() { return usuarioId; }
    public String getCategoriaId() { return categoriaId; }
    public String getEstadoId() { return estadoId; }
    public String getUbicacion() { return ubicacion; }
    public String getImagenUrl() { return imagenUrl; }
    public Instant getFechaCreacion() { return fechaCreacion; }
    public Instant getFechaActualizacion() { return fechaActualizacion; }
    public String getNivelRiesgo() { return nivelRiesgo; }
    public Instant getFechaClasificacion() { return fechaClasificacion; }
    public String getClasificadoPorId() { return clasificadoPorId; }
    public Instant getFechaAprobacion() { return fechaAprobacion; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public void setCategoriaId(String categoriaId) { this.categoriaId = categoriaId; }
    public void setEstadoId(String estadoId) { this.estadoId = estadoId; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setFechaCreacion(Instant fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public void setFechaActualizacion(Instant fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public void setNivelRiesgo(String nivelRiesgo) { this.nivelRiesgo = nivelRiesgo; }
    public void setFechaClasificacion(Instant fechaClasificacion) { this.fechaClasificacion = fechaClasificacion; }
    public void setClasificadoPorId(String clasificadoPorId) { this.clasificadoPorId = clasificadoPorId; }
    public void setFechaAprobacion(Instant fechaAprobacion) { this.fechaAprobacion = fechaAprobacion; }
}