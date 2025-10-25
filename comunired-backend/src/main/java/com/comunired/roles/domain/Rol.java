package com.comunired.roles.domain;

import java.util.Objects;

/**
 * Entidad de dominio Rol.
 * Modelo mutables/defensivo siguiendo el estilo del ejemplo Cuenta:
 * - id inmutable (final) y requerido en el constructor
 * - validaciones en constructor y setters lanzando IllegalArgumentException
 * - equals/hashCode basados en id
 */
public class Rol {

    private final String id;
    private String nombre;
    private String descripcion;

    private static final int NOMBRE_MIN = 2;
    private static final int NOMBRE_MAX = 100;
    private static final int DESCRIPCION_MAX = 1000;

    /**
     * Crea un Rol con id obligatorio.
     *
     * @param id          identificador (no nulo, no vacío)
     * @param nombre      nombre del rol (no nulo, longitud entre NOMBRE_MIN y NOMBRE_MAX)
     * @param descripcion descripción opcional (longitud <= DESCRIPCION_MAX)
     * @throws IllegalArgumentException si alguna validación falla
     */
    public Rol(String id, String nombre, String descripcion) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del rol no puede ser nulo ni vacío");
        }
        validateNombre(nombre);
        validateDescripcionIfPresent(descripcion);

        this.id = id;
        this.nombre = nombre.trim();
        this.descripcion = descripcion == null ? null : descripcion.trim();
    }

    /* ======================
       Validaciones privadas
       ====================== */

    private void validateNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
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
     * Actualiza el nombre del rol (valida invariantes).
     *
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) {
        validateNombre(nombre);
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Actualiza la descripción.
     *
     * @param descripcion nueva descripción (puede ser null)
     */
    public void setDescripcion(String descripcion) {
        validateDescripcionIfPresent(descripcion);
        this.descripcion = descripcion == null ? null : descripcion.trim();
    }

    /* ======================
       equals / hashCode / toString
       ====================== */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rol)) return false;
        Rol rol = (Rol) o;
        return Objects.equals(id, rol.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Rol{" +
            "id='" + id + '\'' +
            ", nombre='" + nombre + '\'' +
            (descripcion != null ? ", descripcion='" + (descripcion.length() <= 60 ? descripcion : descripcion.substring(0, 57) + "...") + '\'' : "") +
            '}';
    }
}