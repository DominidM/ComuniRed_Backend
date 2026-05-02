package com.comunired.quejas.domain.entity;

import java.time.Instant;

public class Queja {

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

    // Clasificación de riesgo
    private String nivelRiesgo;
    private Instant fechaClasificacion;
    private String clasificadoPorId;

    // Aprobación
    private Instant fechaAprobacion;

    // -------------------------------------------------------------------------
    // Constructor privado — solo se instancia por factory methods
    // -------------------------------------------------------------------------
    private Queja() {}

    // -------------------------------------------------------------------------
    // Factory method: nueva queja creada por un ciudadano
    // -------------------------------------------------------------------------
    public static Queja crear(
            String titulo,
            String descripcion,
            String usuarioId,
            String categoriaId,
            String ubicacion,
            String estadoInicialId
    ) {
        Queja q = new Queja();
        q.titulo = titulo;
        q.descripcion = descripcion;
        q.usuarioId = usuarioId;
        q.categoriaId = categoriaId;
        q.ubicacion = ubicacion;
        q.estadoId = estadoInicialId;
        q.fechaCreacion = Instant.now();
        q.fechaActualizacion = Instant.now();
        return q;
    }

    // -------------------------------------------------------------------------
    // Factory method: reconstruir desde persistencia
    // -------------------------------------------------------------------------
    public static Queja reconstruir(
            String id,
            String titulo,
            String descripcion,
            String usuarioId,
            String categoriaId,
            String estadoId,
            String ubicacion,
            String imagenUrl,
            Instant fechaCreacion,
            Instant fechaActualizacion,
            String nivelRiesgo,
            Instant fechaClasificacion,
            String clasificadoPorId,
            Instant fechaAprobacion
    ) {
        Queja q = new Queja();
        q.id = id;
        q.titulo = titulo;
        q.descripcion = descripcion;
        q.usuarioId = usuarioId;
        q.categoriaId = categoriaId;
        q.estadoId = estadoId;
        q.ubicacion = ubicacion;
        q.imagenUrl = imagenUrl;
        q.fechaCreacion = fechaCreacion;
        q.fechaActualizacion = fechaActualizacion;
        q.nivelRiesgo = nivelRiesgo;
        q.fechaClasificacion = fechaClasificacion;
        q.clasificadoPorId = clasificadoPorId;
        q.fechaAprobacion = fechaAprobacion;
        return q;
    }

    // -------------------------------------------------------------------------
    // Comportamiento de dominio
    // -------------------------------------------------------------------------

    public void actualizarDatos(String titulo, String descripcion, String categoriaId,
                                String ubicacion, String imagenUrl) {
        if (titulo != null) this.titulo = titulo;
        if (descripcion != null) this.descripcion = descripcion;
        if (categoriaId != null) this.categoriaId = categoriaId;
        if (ubicacion != null) this.ubicacion = ubicacion;
        if (imagenUrl != null) this.imagenUrl = imagenUrl;
        this.fechaActualizacion = Instant.now();
    }

    public void clasificarRiesgo(String nivelRiesgo, String soporteId) {
        validarNivelRiesgo(nivelRiesgo);
        this.nivelRiesgo = nivelRiesgo.toUpperCase();
        this.clasificadoPorId = soporteId;
        this.fechaClasificacion = Instant.now();
        this.fechaActualizacion = Instant.now();
    }

    public void cambiarEstado(String nuevoEstadoId) {
        this.estadoId = nuevoEstadoId;
        this.fechaActualizacion = Instant.now();
    }

    public void aprobar(String nuevoEstadoId) {
        this.estadoId = nuevoEstadoId;
        this.fechaAprobacion = Instant.now();
        this.fechaActualizacion = Instant.now();
    }

    public void asignarImagen(String imagenUrl) {
        this.imagenUrl = imagenUrl;
        this.fechaActualizacion = Instant.now();
    }

    // -------------------------------------------------------------------------
    // Validaciones privadas
    // -------------------------------------------------------------------------
    private void validarNivelRiesgo(String nivel) {
        if (nivel == null) throw new IllegalArgumentException("El nivel de riesgo no puede ser nulo");
        String upper = nivel.toUpperCase();
        if (!upper.equals("BAJO") && !upper.equals("MEDIO")
                && !upper.equals("ALTO") && !upper.equals("CRITICO")) {
            throw new IllegalArgumentException(
                    "Nivel de riesgo inválido: " + nivel + ". Debe ser BAJO, MEDIO, ALTO o CRITICO");
        }
    }

    // -------------------------------------------------------------------------
    // Getters (sin setters — el estado cambia solo por comportamiento)
    // -------------------------------------------------------------------------
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

    // Solo para persistencia
    public void setId(String id) { this.id = id; }
}
