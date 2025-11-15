package com.comunired.quejas.infrastructure.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "quejas")
public class QuejasModel {
    @Id
    private String id;
    private String titulo;
    private String descripcion;
    private String usuario_id;
    private String categoria_id;
    private String estado_id;
    private String ubicacion;
    private String imagen_url;
    private Instant fecha_creacion;
    private Instant fecha_actualizacion;

    public QuejasModel() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUsuario_id() { return usuario_id; }
    public void setUsuario_id(String usuario_id) { this.usuario_id = usuario_id; }

    public String getCategoria_id() { return categoria_id; }
    public void setCategoria_id(String categoria_id) { this.categoria_id = categoria_id; }

    public String getEstado_id() { return estado_id; }
    public void setEstado_id(String estado_id) { this.estado_id = estado_id; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getImagen_url() { return imagen_url; }
    public void setImagen_url(String imagen_url) { this.imagen_url = imagen_url; }

    public Instant getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(Instant fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    public Instant getFecha_actualizacion() { return fecha_actualizacion; }
    public void setFecha_actualizacion(Instant fecha_actualizacion) { this.fecha_actualizacion = fecha_actualizacion; }
}
