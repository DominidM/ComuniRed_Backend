package com.comunired.comentarios.infrastructure.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "comentarios")
public class ComentariosModel {
    @Id
    private String id;
    private String queja_id;
    private String usuario_id;
    private String texto;
    private Instant fecha_creacion;
    private Instant fecha_modificacion;
    

    private Boolean eliminado;
    private String eliminado_por;
    private String razon_eliminacion;
    private Instant fecha_eliminacion;

    public ComentariosModel() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQueja_id() { return queja_id; }
    public void setQueja_id(String queja_id) { this.queja_id = queja_id; }

    public String getUsuario_id() { return usuario_id; }
    public void setUsuario_id(String usuario_id) { this.usuario_id = usuario_id; }

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
}
