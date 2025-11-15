package com.comunired.reacciones.application.dto;

import com.comunired.usuarios.application.dto.UsuariosDTO;
import com.comunired.tipos_reaccion.application.dto.Tipos_reaccionDTO;
import java.time.Instant;

public class ReaccionesDTO {
    private String id;
    private String queja_id;
    private UsuariosDTO usuario;
    private Tipos_reaccionDTO tipoReaccion;
    private Instant fecha_reaccion;

    public ReaccionesDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQueja_id() { return queja_id; }
    public void setQueja_id(String queja_id) { this.queja_id = queja_id; }

    public UsuariosDTO getUsuario() { return usuario; }
    public void setUsuario(UsuariosDTO usuario) { this.usuario = usuario; }

    public Tipos_reaccionDTO getTipoReaccion() { return tipoReaccion; }
    public void setTipoReaccion(Tipos_reaccionDTO tipoReaccion) { this.tipoReaccion = tipoReaccion; }

    public Instant getFecha_reaccion() { return fecha_reaccion; }
    public void setFecha_reaccion(Instant fecha_reaccion) { this.fecha_reaccion = fecha_reaccion; }
}
