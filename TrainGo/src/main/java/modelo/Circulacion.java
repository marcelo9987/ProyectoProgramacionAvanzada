package modelo;

import modelo.Enums.EnumCirculacion;

import java.util.Currency;
import java.util.Date;
import java.util.UUID;

/**
 * Clase Circulacion
 * Modela la circulación de un tren por una ruta
 *
 * @apiNote Una circulación es el viaje de un tren por una ruta
 */
public final class Circulacion {
    private final UUID id;
    private Tren tren;
    private Ruta ruta;
    private EnumCirculacion estado;
    private Date horaSalidaReal;
    private Date horaLlegadaReal;
    private Currency precioPorAsiento;


    public Circulacion(UUID id, Tren tren, Ruta ruta, EnumCirculacion estado, Date horaSalidaReal, Date horaLlegadaReal, Currency precioPorAsiento) {
        this.id = id;
        this.tren = tren;
        this.ruta = ruta;
        this.estado = estado;
        this.horaSalidaReal = horaSalidaReal;
        this.horaLlegadaReal = horaLlegadaReal;
        this.precioPorAsiento = precioPorAsiento;
    }

    public Circulacion(UUID id, Tren tren, Ruta ruta, EnumCirculacion estado, Date horaSalidaReal, Date horaLlegadaReal) {
        this.id = id;
        this.tren = tren;
        this.ruta = ruta;
        this.estado = estado;
        this.horaSalidaReal = horaSalidaReal;
        this.horaLlegadaReal = horaLlegadaReal;
    }

    public Circulacion(Tren tren, Ruta ruta, EnumCirculacion estado) {
        this(UUID.randomUUID(), tren, ruta, estado, null, null);
    }

}
