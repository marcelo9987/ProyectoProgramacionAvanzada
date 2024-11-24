package modelo;

import util.criptograficos;

import java.util.Date;

public final class Usuario {
    private final int DNI;
    private String nombre;
    private String correo;
    /**
     * Contraseña del usuario
     *
     * @implNote La contraseña debe tener al menos 8 caracteres.
     * Esta es almacenada encriptada mediante ÑÑÑÑÑ.
     * @see ÑÑÑÑLLLLXXXX
     * @since 1.0 20/11/2024
     */
    private String contrasenha;
    private int telefono;
    private String direccion;
    private Date fechaNacimiento;

    public Usuario(int DNI, String nombre, String correo, String contrasenha, int telefono, String direccion, Date fechaNacimiento) {
        this.DNI = DNI;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenha = criptograficos.cifrar(contrasenha);
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
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
                + fechaNacimiento + '}';
    }
}
