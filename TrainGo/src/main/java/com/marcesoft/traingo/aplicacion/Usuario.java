package com.marcesoft.traingo.aplicacion;

import jakarta.validation.constraints.Email;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import com.marcesoft.traingo.util.Criptograficos;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase Usuario
 * Modela un usuario del sistema
 */
public final class Usuario {
    private final int    DNI;
    private final String nombre;
    private final LocalDate fechaNacimiento;
    /**
     * Contraseña del usuario
     *  Debe tener al menos 8 caracteres.
     * Esta es almacenada encriptada mediante DES.
     * @see Criptograficos#cifrar(String)
     * @since 1.0 20/11/2024
     */
    private final String contrasenha;
    @Email
    private       String correo;
    private       int    telefono;
    private       String direccion;

    /**
     * @param DNI             DNI del usuario
     * @param nombre          Nombre del usuario
     * @param correo          Correo del usuario
     * @param contrasenha     Contraseña del usuario
     * @param telefono        Teléfono del usuario
     * @param direccion       Dirección del usuario
     * @param fechaNacimiento Fecha de nacimiento del usuario
     * @param cifrar          Indica si la contraseña debe ser cifrada. Si es true, la contraseña se cifra.
     */
    public Usuario(int DNI, String nombre, String correo, String contrasenha, int telefono, String direccion, LocalDate fechaNacimiento, boolean cifrar) {
        super();
        this.DNI = DNI;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenha = cifrar ? Criptograficos.cifrar(contrasenha) : contrasenha;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Devuelve una cadena con los datos del usuario listos para ser guardados en un archivo.
     *
     * @return Cadena con los datos del usuario listos para ser guardados en un archivo.
     * @deprecated Previo a la migración a XML. No se recomienda su uso.
     */
    @NotNull
    @Contract(pure = true)
    @Deprecated
    public String printReadyString() {
        return "Usuario{" + dni() + "," + nombre + "," + correo + "," + contrasenha + "," + telefono + "," + direccion + "," + fechaNacimiento.toString().substring(0, 10) + fechaNacimiento.toString().substring(23) + '}';
    }

    /**
     * Devuelve el DNI del usuario.
     *
     * @return DNI del usuario.
     */
    public int dni() {
        return DNI;
    }

    /**
     * Devuelve el DNI del usuario.
     *
     * @return DNI del usuario.
     */
    public int DNI() {
        return dni();
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni());
    }

    /**
     * Compara dos objetos de tipo Usuario.
     *
     * @param o Usuario a comparar.
     * @return true si los DNI de los usuarios son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) {
            return false;
        }

        return dni() == usuario.dni();
    }

    /**
     * Imprime los datos del usuario.
     *
     * @return Cadena con los datos del usuario.
     */
    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Usuario{" + "DNI=" + dni() + ", nombre='" + nombre + '\'' + ", correo='" + correo + '\'' + ", contrasenha='" + contrasenha + '\'' + ", telefono=" + telefono + ", direccion='" + direccion + '\'' + ", fechaNacimiento=" + fechaNacimiento.toString() + '}';
    }

    /**
     * Devuelve la contraseña del usuario.
     *
     * @return Contraseña del usuario.
     */
    public String contrasenha() {
        return contrasenha;
    }

    /**
     * Devuelve el correo del usuario.
     *
     * @return Correo del usuario.
     */
    public String correo() {
        return correo;
    }

    /**
     * Devuelve la dirección del usuario.
     *
     * @return Dirección del usuario.
     */
    public String direccion() {
        return direccion;
    }

    /**
     * Devuelve la fecha de nacimiento del usuario.
     *
     * @return Fecha de nacimiento del usuario.
     */
    public LocalDate fechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Devuelve el nombre del usuario.
     *
     * @return Nombre del usuario.
     */
    public String nombre() {
        return nombre;
    }

    /**
     * Devuelve el teléfono del usuario.
     *
     * @return Teléfono del usuario.
     */
    public int telefono() {
        return telefono;
    }

    /**
     * Actualiza los datos del usuario.
     *
     * @param nuevoCorreo    Nuevo correo del usuario.
     * @param nuevaDireccion Nueva dirección del usuario.
     * @param nuevoTelefono  Nuevo teléfono del usuario.
     */
    public void actualizarDatos(@Email String nuevoCorreo, String nuevaDireccion, int nuevoTelefono) {
        this.correo = nuevoCorreo;
        this.direccion = nuevaDireccion;
        this.telefono = nuevoTelefono;
    }
}
