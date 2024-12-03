package aplicacion;

import aplicacion.Enums.EnumCirculacion;
import dao.FachadaDAO;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Clase Circulacion
 * Modela la circulación de un tren por una ruta
 *
 * @apiNote Una circulación es el viaje de un tren por una ruta
 */

public final class Circulacion implements Comparable {
    private final UUID            id;
    private final Tren            tren;
    private final Ruta            ruta;
    private final EnumCirculacion estado;
    private final LocalDateTime   horaSalida;
    private final BigDecimal      precioPorAsiento;
    @Nullable
    private       LocalDateTime   horaLlegadaReal;


    public Circulacion(UUID id, Tren tren, Ruta ruta, EnumCirculacion estado, LocalDateTime horaSalida, @Nullable LocalDateTime horaLlegadaReal, BigDecimal precioPorAsiento) {
        super();
        this.id = id;
        this.tren = tren;
        this.ruta = ruta;
        this.estado = estado;
        this.horaSalida = horaSalida;
        this.horaLlegadaReal = horaLlegadaReal;
        this.precioPorAsiento = precioPorAsiento;
    }

    public Circulacion(UUID id, Tren tren, Ruta ruta, EnumCirculacion estado, LocalDateTime horaSalida, BigDecimal precioPorAsiento) {
        super();
        this.id = id;
        this.tren = tren;
        this.ruta = ruta;
        this.estado = estado;
        this.horaSalida = horaSalida;
        this.precioPorAsiento = precioPorAsiento;
    }

    @Nullable
    public static Circulacion fabricarCirculacion(@NonNls String trenId, UUID id, Ruta ruta, EnumCirculacion estado, LocalDateTime horaSalida, LocalDateTime horaLlegadaReal, BigDecimal precioPorAsiento) {
        FachadaDAO fa_dao        = FachadaDAO.getInstance();
        Logger     static_logger = org.slf4j.LoggerFactory.getLogger(Circulacion.class);

        static_logger.debug("Empiezo a parsear la circulación");
        Tren tren = fa_dao.localizarTren(trenId);
        if (tren == null) {
            static_logger.error("No se ha encontrado el tren {}: Error al crear la circulación", trenId);

            return null;
        }

        if (id == null) {
            static_logger.error("El id de la circulación no puede ser nulo: Error al crear la circulación");

            return null;
        }

        if (ruta == null) {
            static_logger.error("La ruta de la circulación no puede ser nula: Error al crear la circulación");

            return null;
        }

        if (estado == null) {
            static_logger.error("El estado de la circulación no puede ser nulo: Error al crear la circulación");

            return null;
        }

        if (horaSalida == null) {
            static_logger.error("La hora de salida de la circulación no puede ser nula: Error al crear la circulación");

            return null;
        }

        if (precioPorAsiento == null) {
            static_logger.error("El precio por asiento de la circulación no puede ser nulo: Error al crear la circulación");

            return null;
        }

        if (precioPorAsiento.compareTo(BigDecimal.ZERO) < 0) {
            static_logger.error("El precio por asiento de la circulación no puede ser negativo: Error al crear la circulación");

            return null;
        }

        if (horaLlegadaReal != null && horaLlegadaReal.isBefore(horaSalida)) {
            static_logger.error("La hora de llegada real no puede ser anterior a la hora de salida: Error al crear la circulación");

            return null;
        }

        static_logger.trace("Tren encontrado: {}", tren);

        return new Circulacion(id, tren, ruta, estado, horaSalida, horaLlegadaReal, precioPorAsiento);
    }


    public String ciudadOrigen() {
        return this.ruta.ciudadOrigen();
    }

    public String ciudadDestino() {
        return this.ruta.ciudadDestino();
    }

    public LocalTime getHoraSalida() {
        return this.horaSalida.toLocalTime();
    }

    public BigDecimal getPrecioPorAsiento() {
        return this.precioPorAsiento;
    }

    public EnumCirculacion estado() {
        return estado;
    }

    @Nullable
    public LocalDateTime horaLlegadaReal() {
        return horaLlegadaReal;
    }

    public UUID id() {
        return id;
    }

    public BigDecimal precioPorAsiento() {
        return precioPorAsiento;
    }

    public Ruta ruta() {
        return ruta;
    }

    public Tren tren() {
        return tren;
    }

    public UUID trenId() {
        return tren.id();
    }

    @Nullable
    @Contract(pure = true)
    public String nombreCiudadOrigen() {
        return ruta == null ? null : ruta.ciudadOrigen();
    }

    @Nullable
    @Contract(pure = true)
    public String nombreCiudadDestino() {
        return ruta == null ? null : ruta.ciudadDestino();
    }

    @NotNull
    @Contract(pure = true)
    public String getCadenaHoraFechaSalida() {
        // formato: dd/MM/yyyy HH:mm
        return horaSalida.toLocalDate().toString() + " " + getCadenaHoraSalida();
    }

    @NotNull
    @Contract(pure = true)
    public String getCadenaHoraSalida() {
        int hora   = horaSalida.getHour();
        int minuto = horaSalida.getMinute();
        return new String(((hora < 10) ? "0" : "") + hora + ":" + ((minuto < 10) ? "0" : "") + minuto);
    }

    @Override
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

    public LocalDateTime horaSalida() {
        return horaSalida;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Circulacion that)) {
            return false;
        }

        return id.equals(that.id);
    }

    @NotNull
    @Contract(pure = true)
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
