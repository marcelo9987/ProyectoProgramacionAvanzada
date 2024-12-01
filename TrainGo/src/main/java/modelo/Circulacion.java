package modelo;

import modelo.Enums.EnumCirculacion;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Currency;
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
    private LocalDateTime horaSalida;
    private LocalDateTime horaLlegadaReal;
    private Currency precioPorAsiento;


    public Circulacion(UUID id, Tren tren, Ruta ruta, EnumCirculacion estado, LocalDateTime horaSalida, LocalDateTime horaLlegadaReal, Currency precioPorAsiento) {
        super();
        this.id = id;
        this.tren = tren;
        this.ruta = ruta;
        this.estado = estado;
        this.horaSalida = horaSalida;
        this.horaLlegadaReal = horaLlegadaReal;
        this.precioPorAsiento = precioPorAsiento;
    }

    public Circulacion(UUID id, Tren tren, Ruta ruta, EnumCirculacion estado, LocalDateTime horaSalida, Currency precioPorAsiento) {
        super();
        this.id = id;
        this.tren = tren;
        this.ruta = ruta;
        this.estado = estado;
        this.horaSalida = horaSalida;
        this.precioPorAsiento = precioPorAsiento;
    }

//    public Circulacion(Tren tren, Ruta ruta, EnumCirculacion estado) {
//        this(UUID.randomUUID(), tren, ruta, estado, null, null);
//    }

    public String ciudadOrigen() {
        return this.ruta.ciudadOrigen();
    }

    public String ciudadDestino() {
        return this.ruta.ciudadDestino();
    }

    public LocalTime getHoraSalida() {
        return this.horaSalida.toLocalTime();
    }

    public Currency getPrecioPorAsiento() {
        return this.precioPorAsiento;
    }

    @Override
    public String toString() {
        return "Circulacion{" +
                "id=" + id +
                ", tren=" + tren +
                ", ruta=" + ruta +
                ", estado=" + estado +
                ", horaSalida=" + horaSalida +
                ", horaLlegadaReal=" + horaLlegadaReal +
                ", precioPorAsiento=" + precioPorAsiento +
                '}';
    }
}
