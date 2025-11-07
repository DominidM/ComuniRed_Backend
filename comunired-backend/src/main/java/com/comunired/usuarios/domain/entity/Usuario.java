package com.comunired.usuarios.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Instant;

@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    private String foto_perfil;
    private String nombre;
    private String apellido;
    private String dni;
    private String numero_telefono;

    private String sexo;
    private String distrito;
    private String codigo_postal;
    private String direccion;
    private String email;

    private LocalDate fecha_nacimiento; 
    private Instant  fecha_registro;

    @JsonIgnore
    private String password;
    private String rol_id;
    
    @JsonIgnore
    private String resetPasswordToken;
    @JsonIgnore
    private LocalDateTime resetPasswordExpiry;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFoto_perfil() {
        return foto_perfil;
    }
    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getNumero_telefono() {
        return numero_telefono;
    }
    public void setNumero_telefono(String numero_telefono) {
        this.numero_telefono = numero_telefono;
    }
    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public String getDistrito() {
        return distrito;
    }
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
    public String getCodigo_postal() {
        return codigo_postal;
    }
    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRol_id() {
        return rol_id;
    }
    public void setRol_id(String rol_id) {
        this.rol_id = rol_id;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }
    
    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }
    
    public LocalDateTime getResetPasswordExpiry() {
        return resetPasswordExpiry;
    }
    
    public void setResetPasswordExpiry(LocalDateTime resetPasswordExpiry) {
        this.resetPasswordExpiry = resetPasswordExpiry;
    }

    public LocalDate getFecha_nacimiento() { return fecha_nacimiento; }
    public void setFecha_nacimiento(LocalDate fecha_nacimiento) { this.fecha_nacimiento = fecha_nacimiento; }

    public Instant getFecha_registro() { 
        return fecha_registro; 
    }
    
    public void setFecha_registro(Instant fecha_registro) { 
        this.fecha_registro = fecha_registro; 
    }

}