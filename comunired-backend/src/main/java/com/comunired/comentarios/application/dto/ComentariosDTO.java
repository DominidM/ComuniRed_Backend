package com.comunired.comentarios.application.dto;

import com.comunired.usuarios.application.dto.UsuariosDTO;
import java.time.Instant;

public class ComentariosDTO {
    private String id;
    private String queja_id;
    private UsuariosDTO author;
    private String texto;
    private Instant fecha_creacion;
    private Instant fecha_modificacion;
    
    private Boolean eliminado;
    private String eliminado_por;
    private String razon_eliminacion;
    private Instant fecha_eliminacion;

    private String quejaTitulo;
    private String quejaDescripcion;
    private String quejaImagenUrl;

    public ComentariosDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQueja_id() { return queja_id; }
    public void setQueja_id(String queja_id) { this.queja_id = queja_id; }

    public UsuariosDTO getAuthor() { return author; }
    public void setAuthor(UsuariosDTO author) { this.author = author; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Instant getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(Instant fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    public Instant getFecha_modificacion() { return fecha_modificacion; }
    public void setFecha_modificacion(Instant fecha_modificacion) { this.fecha_modificacion = fecha_modificacion; }

    public Boolean getEliminado() { return eliminado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }

    public String getEliminado_por() { return eliminado_por; }
    public void setEliminado_por(String eliminado_por) { this.eliminado_por = eliminado_por; }

    public String getRazon_eliminacion() { return razon_eliminacion; }
    public void setRazon_eliminacion(String razon_eliminacion) { this.razon_eliminacion = razon_eliminacion; }

    public Instant getFecha_eliminacion() { return fecha_eliminacion; }
    public void setFecha_eliminacion(Instant fecha_eliminacion) { this.fecha_eliminacion = fecha_eliminacion; }

        public String getQuejaTitulo() {
        return quejaTitulo;
    }

    public void setQuejaTitulo(String quejaTitulo) {
        this.quejaTitulo = quejaTitulo;
    }

    public String getQuejaDescripcion() {
        return quejaDescripcion;
    }

    public void setQuejaDescripcion(String quejaDescripcion) {
        this.quejaDescripcion = quejaDescripcion;
    }

    public String getQuejaImagenUrl() { return quejaImagenUrl; }
    public void setQuejaImagenUrl(String quejaImagenUrl) { this.quejaImagenUrl = quejaImagenUrl; }
}
