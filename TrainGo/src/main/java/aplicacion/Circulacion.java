package aplicacion;

import aplicacion.anotaciones.NoNegativo;
import aplicacion.enums.EnumCirculacion;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase Circulacion
 * Modela la circulación de un tren por una ruta
 *
 * @apiNote Una circulación es el viaje de un tren por una ruta
 */
public record Circulacion(@org.hibernate.validator.constraints.UUID UUID id, Tren tren, Ruta ruta, EnumCirculacion estado, LocalDateTime horaSalida, @Nullable LocalDateTime horaLlegadaReal, @NoNegativo BigDecimal precioPorAsiento) implements Comparable {
    /**
     * Constructor de Circulacion
     *
     * @param id               Identificador de la circulación
     * @param tren             Tren que realiza la circulación
     * @param ruta             Ruta por la que circula el tren (origen y destino)
     * @param estado           Estado de la circulación. Puede ser EN_TRANSITO, PROGRAMADO, FINALIZADO o CANCELADO
     * @param horaSalida       Hora de salida de la circulación
     * @param horaLlegadaReal  Hora de llegada real de la circulación (opcional)
     * @param precioPorAsiento Precio por asiento de la circulación
     * @see EnumCirculacion
     */
    public Circulacion(@org.hibernate.validator.constraints.UUID UUID id, Tren tren, Ruta ruta, @MagicConstant(valuesFromClass = EnumCirculacion.class) EnumCirculacion estado, LocalDateTime horaSalida, @Nullable LocalDateTime horaLlegadaReal, @NoNegativo BigDecimal precioPorAsiento) {
        this.id = id;
        this.tren = tren;
        this.ruta = ruta;
        this.estado = estado;
        this.horaSalida = horaSalida;
        this.horaLlegadaReal = horaLlegadaReal;
        this.precioPorAsiento = precioPorAsiento;
    }


    /**
     * @return Ciudad de origen de la circulación
     */
    @Contract(pure = true)
    public String ciudadOrigen() {
        return this.ruta.ciudadOrigen();
    }

    /**
     * @return Ciudad a la que se dirige la circulación
     */
    @Contract(pure = true)
    public String ciudadDestino() {
        return this.ruta.ciudadDestino();
    }

    /**
     * @return Identificador del tren que realiza la circulación
     */
    @NotNull
    @Contract(pure = true)
    public UUID trenId() {
        return tren.id();
    }

    /**
     * Devuelve el nombre de la ciudad de origen de la circulación
     *
     * @return null si la ruta es nula, el nombre de la ciudad de origen en otro caso
     */
    @Nullable
    @Contract(pure = true)
    public String nombreCiudadOrigen() {
        return ruta == null ? null : ruta.ciudadOrigen();
    }

    /**
     * Devuelve el nombre de la ciudad de destino de la circulación
     *
     * @return null si la ruta es nula, el nombre de la ciudad de destino en otro caso
     */
    @Nullable
    @Contract(pure = true)
    public String nombreCiudadDestino() {
        return ruta == null ? null : ruta.ciudadDestino();
    }

    /**
     * Devuelve la cadena de la hora y fecha de salida de la circulación
     *
     * @return Cadena con la hora y fecha de salida de la circulación
     */
    @NotNull
    @Contract(pure = true)
    public String getCadenaHoraFechaSalida() {
        // formato: dd/MM/yyyy HH:mm
        return horaSalida.toLocalDate().toString() + " " + getCadenaHoraSalida();
    }

    /**
     * Devuelve la cadena de la hora de salida de la circulación
     *
     * @return Cadena con la hora de salida de la circulación
     */
    @NotNull
    @Contract(pure = true)
    private String getCadenaHoraSalida() {
        int hora   = horaSalida.getHour();
        int minuto = horaSalida.getMinute();
        return ((hora < 10) ? "0" : "") + hora + ":" + ((minuto < 10) ? "0" : "") + minuto;
    }

    /**
     * Compara dos circulaciones por la hora de salida
     *
     * @param o Circulación con la que se compara
     * @return -1 si la hora de salida de esta circulación es anterior a la de la otra, 1 si es posterior, 0 si son iguales
     * @see Comparable
     */
    @Override
    @Contract(pure = true)
    public int compareTo(@NotNull Object o) {
        Circulacion otro = (Circulacion) o;
        if (this.horaSalida.isBefore(otro.horaSalida())) {
            return -1;
        }
        if (this.horaSalida.isAfter(otro.horaSalida())) {
            return 1;
        }
        return 0;
    }


    @Contract(pure = true)
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Circulacion that)) {
            return false;
        }

        return id().equals(that.id());
    }

    @Contract(pure = true)
    @Override
    public int hashCode() {
        return id().hashCode();
    }


    /**
     * Devuelve una cadena con la información de la circulación
     *
     * @return Cadena con la información de la circulación
     */
    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Circulacion{" +
                "id=" + id() +
                ", tren=" + tren +
                ", ruta=" + ruta +
                ", estado=" + estado +
                ", horaSalida=" + horaSalida +
                ", horaLlegadaReal=" + horaLlegadaReal +
                ", precioPorAsiento=" + precioPorAsiento +
                '}';
    }

    @Contract(pure = true)
    int trenNumero() {
        return tren.num();
    }

    /**
     * Comprueba si la circulación está programada
     *
     * @return true si la circulación está programada, false en otro caso
     */
    @Contract(pure = true)
    public boolean programada() {
        return estado == EnumCirculacion.PROGRAMADO;
    }
}
