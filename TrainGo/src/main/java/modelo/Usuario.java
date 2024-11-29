package modelo;

import util.Criptograficos;

import java.util.Date;

public final class Usuario {
    private final int DNI;
    private final String nombre;
    private final String correo;
    /**
     * Contraseña del usuario
     *
     * @implNote La contraseña debe tener al menos 8 caracteres.
     * Esta es almacenada encriptada mediante DES.
     * @see Criptograficos
     * @since 1.0 20/11/2024
     */
    private final String contrasenha;
    private final int telefono;
    private final String direccion;
    private final Date fechaNacimiento;

    public Usuario(int DNI, String nombre, String correo, String contrasenha, int telefono, String direccion, Date fechaNacimiento, boolean cifrar) {
        this.DNI = DNI;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenha = cifrar ? Criptograficos.cifrar(contrasenha) : contrasenha;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
    }


    public String correo() {
        return correo;
    }

    public String contrasenha() {
        return contrasenha;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "DNI=" + DNI +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasenha='" + contrasenha + '\'' +
                ", telefono=" + telefono +
                ", direccion='" + direccion + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }

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
}
