package com.comunired.usuarios.application.dto;

import com.comunired.usuarios.domain.entity.Usuario;

public class AuthPayload {
    private String token;
    private Usuario usuario;

    public AuthPayload() {}

    public AuthPayload(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}