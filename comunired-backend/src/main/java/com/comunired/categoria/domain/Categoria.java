package com.comunired.categoria.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entidad de dominio Categoria.
 * Estilo similar al ejemplo previamente solicitado: id inmutable (final),
 * validaciones en constructor y en setters, igualdad basada en id.
 */
public class Categoria implements Serializable {

    private static final int NOMBRE_MIN = 2;
    private static final int NOMBRE_MAX = 100;
    private static final int DESCRIPCION_MAX = 1000;

    private final String id;
    private String nombre;
    private String descripcion;
    private boolean activo;

    /**
     * Constructor principal: requiere id y nombre.
     *
     * @param id          Identificador (no nulo, no vacío)
     * @param nombre      Nombre de la categoría (obligatorio)
     * @param descripcion Descripción opcional
     * @param activo      Indicador si la categoría está activa
     */
    public Categoria(String id, String nombre, String descripcion, Boolean activo) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id de la categoría no puede ser nulo ni vacío");
        }
        validateNombre(nombre);
        validateDescripcionIfPresent(descripcion);

        this.id = id;
        this.nombre = nombre.trim();
        this.descripcion = descripcion == null ? null : descripcion.trim();
        this.activo = activo == null ? true : activo.booleanValue();
    }

    /**
     * Constructor de conveniencia cuando se crea una categoría nueva (activo = true).
     *
     * @param id     Identificador
     * @param nombre Nombre
     * @param descripcion Descripción opcional
     */
    public Categoria(String id, String nombre, String descripcion) {
        this(id, nombre, descripcion, true);
    }

    /* ======================
       Validaciones privadas
       ====================== */

    private void validateNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        String trimmed = nombre.trim();
        if (trimmed.length() < NOMBRE_MIN || trimmed.length() > NOMBRE_MAX) {
            throw new IllegalArgumentException(
                String.format("El nombre debe tener entre %d y %d caracteres", NOMBRE_MIN, NOMBRE_MAX)
            );
        }
    }

    private void validateDescripcionIfPresent(String descripcion) {
        if (descripcion == null) return;
        if (descripcion.length() > DESCRIPCION_MAX) {
            throw new IllegalArgumentException(
                "La descripción excede la longitud permitida de " + DESCRIPCION_MAX + " caracteres"
            );
        }
    }

    /* ======================
       Getters / Setters
       ====================== */

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Actualiza el nombre de la categoría (valida invariantes).
     *
     * @param nombre nuevo nombre (no nulo, rango válido)
     */
    public void setNombre(String nombre) {
        validateNombre(nombre);
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Actualiza la descripción (puede ser null).
     *
     * @param descripcion nueva descripción
     */
    public void setDescripcion(String descripcion) {
        validateDescripcionIfPresent(descripcion);
        this.descripcion = descripcion == null ? null : descripcion.trim();
    }

    public boolean isActivo() {
        return activo;
    }

    /**
     * Activa o desactiva la categoría.
     *
     * @param activo nuevo estado
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Conveniencia para desactivar la categoría.
     */
    public void deactivate() {
        this.activo = false;
    }

    /**
     * Conveniencia para activar la categoría.
     */
    public void activate() {
        this.activo = true;
    }

    /* ======================
       equals / hashCode / toString
       ====================== */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria that = (Categoria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Categoria{" +
            "id='" + id + '\'' +
            ", nombre='" + nombre + '\'' +
            (descripcion != null ? ", descripcion='" + (descripcion.length() <= 60 ? descripcion : descripcion.substring(0, 57) + "...") + '\'' : "") +
            ", activo=" + activo +
            '}';
    }
}