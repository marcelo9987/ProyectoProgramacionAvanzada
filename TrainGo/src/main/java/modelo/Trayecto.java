package modelo;

import java.util.Currency;
import java.util.Date;

public class Trayecto {
    private Estacion origen;
    private Estacion destino;
    private int distancia;
    private int duracion;
    private Date horaSalida;
    private Currency precio;
    private Tren trenAsignado;

    public Trayecto(Estacion origen, Estacion destino, int distancia, int duracion, Currency precio) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.duracion = duracion;
        this.precio = precio;
    }

    public boolean asignarTren(Tren tren) {
        if (trenAsignado == null) {
            trenAsignado = tren;
            return true;
        }
        return false;
    }

    public void asignarOcambiarTren(Tren tren) {
        trenAsignado = tren;
    }

    public boolean quitarTren() {
        if (trenAsignado != null) {
            trenAsignado = null;
            return true;
        }
        return false;
    }


}
