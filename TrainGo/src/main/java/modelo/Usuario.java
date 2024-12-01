package modelo;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.Criptograficos;

import java.time.LocalDate;
import java.util.Objects;

public final class Usuario {
    private final int DNI;
    private final String nombre;
    private final LocalDate fechaNacimiento;
    /**
     * Contraseña del usuario
     *
     * @implNote La contraseña debe tener al menos 8 caracteres.
     * Esta es almacenada encriptada mediante DES.
     * @see Criptograficos#cifrar(String)
     * @since 1.0 20/11/2024
     */
    private final String contrasenha;
    private String correo;
    private int telefono;
    private String direccion;

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

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Usuario{" +
                "DNI=" + DNI +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasenha='" + contrasenha + '\'' +
                ", telefono=" + telefono +
                ", direccion='" + direccion + '\'' +
                ", fechaNacimiento=" + fechaNacimiento.toString() +
                '}';
    }

    /**
     * Devuelve una cadena con los datos del usuario listos para ser guardados en un archivo.
     *
     * @return Cadena con los datos del usuario listos para ser guardados en un archivo.
     * @deprecated Previo a la migración a XML. No se recomienda su uso.
     *
     */
    @NotNull
    @Deprecated
    public String printReadyString() {
        return "Usuario{"
                + DNI + ","
                + nombre + ","
                + correo + ","
                + contrasenha + ","
                + telefono + ","
                + direccion + ","
                + fechaNacimiento.toString().substring(0, 10) + fechaNacimiento.toString().substring(23) + '}';
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) {
            return false;
        }

        return DNI == usuario.DNI;
    }

    public int DNI() {
        return DNI;
    }

    public String contrasenha() {
        return contrasenha;
    }

    public String correo() {
        return correo;
    }

    public String direccion() {
        return direccion;
    }

    public LocalDate fechaNacimiento() {
        return fechaNacimiento;
    }

    public String nombre() {
        return nombre;
    }

    public int telefono() {
        return telefono;
    }


    public void actualizarDatos(String nuevoCorreo, String nuevaDireccion, int nuevoTelefono) {
        this.correo = nuevoCorreo;
        this.direccion = nuevaDireccion;
        this.telefono = nuevoTelefono;
    }


    @Override
    public int hashCode() {
        return Objects.hash(Integer.valueOf(DNI));
    }


}
