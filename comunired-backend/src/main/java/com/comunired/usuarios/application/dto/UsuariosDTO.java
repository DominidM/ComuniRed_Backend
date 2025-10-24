package com.comunired.usuarios.application.dto;

import com.comunired.roles.application.dto.RolDTO;

public class UsuariosDTO {
    private String id;
    private String foto_perfil;
    private String nombre;
    private String apellido;
    private String dni;
    private String numero_telefono;
    private Integer edad;
    private String sexo;
    private String distrito;
    private String codigo_postal;
    private String direccion;
    private String email;
    private String rol_id;

    private RolDTO rol;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFoto_perfil() { return foto_perfil; }
    public void setFoto_perfil(String foto_perfil) { this.foto_perfil = foto_perfil; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNumero_telefono() { return numero_telefono; }
    public void setNumero_telefono(String numero_telefono) { this.numero_telefono = numero_telefono; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }

    public String getCodigo_postal() { return codigo_postal; }
    public void setCodigo_postal(String codigo_postal) { this.codigo_postal = codigo_postal; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol_id() { return rol_id; }
    public void setRol_id(String rol_id) { this.rol_id = rol_id; }

    public RolDTO getRol() { return rol; }
    public void setRol(RolDTO rol) { this.rol = rol; }
}