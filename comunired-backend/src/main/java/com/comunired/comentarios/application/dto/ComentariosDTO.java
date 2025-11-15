package com.comunired.comentarios.application.dto;

import com.comunired.usuarios.application.dto.UsuariosDTO;
import java.time.Instant;

public class ComentariosDTO {
    private String id;
    private String queja_id;
    private UsuariosDTO author;
    private String texto;
    private Instant fecha_creacion;

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
}
