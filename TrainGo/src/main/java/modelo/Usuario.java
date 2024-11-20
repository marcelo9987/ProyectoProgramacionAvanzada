package modelo;

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

    public Usuario(String contrasenha, String correo, String direccion, int DNI, Date fechaNacimiento, String nombre, int telefono) {
        this.contrasenha = contrasenha;
        this.correo = correo;
        this.direccion = direccion;
        this.DNI = DNI;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.telefono = telefono;
    }

}
