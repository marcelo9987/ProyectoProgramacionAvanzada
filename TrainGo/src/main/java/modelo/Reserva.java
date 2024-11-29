package modelo;

import java.util.Date;

public class Reserva {
    Usuario usuario;
    Trayecto trayecto;
    Date fechaReserva;

    public Reserva(Usuario usuario, Trayecto trayecto, Date fechaReserva) {
        this.usuario = usuario;
        this.trayecto = trayecto;
        this.fechaReserva = fechaReserva;
    }
}
