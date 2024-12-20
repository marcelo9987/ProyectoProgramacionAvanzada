package com.marcesoft.traingo.aplicacion;

import com.marcesoft.traingo.aplicacion.enums.EnumCirculacion;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 *  Clase que representa una reserva
 * @param id          Identificador de la reserva
 * @param usuario     Usuario que ha realizado la reserva
 * @param circulacion Circulación reservada
 */
public record Reserva(@org.hibernate.validator.constraints.UUID UUID id, Usuario usuario, Circulacion circulacion) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor de la clase Reserva
     * @param usuario Usuario que ha realizado la reserva
     * @param circulacion Circulación reservada
     */
    public Reserva(Usuario usuario, Circulacion circulacion) {
        this(UUID.randomUUID(), usuario, circulacion);
    }


    @Override
    public int hashCode() {
        return id().hashCode();
    }

    /**
     * Indica si dos reservas son iguales
     *
     * @param o Objeto a comparar
     * @return true si tienen el mismo id o si el usuario y la circulación son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Reserva(UUID id1, Usuario usuario1, Circulacion circulacion1))) {
            return false;
        }
        return id().equals(id1) || (usuario().equals(usuario1) && circulacion().equals(circulacion1));
    }


    /**
     * Obtiene la fecha y hora de la salida en un formato imprimible
     * @return Fecha y hora de salida de la circulación en formato imprimible
     */
    @NotNull
    public String fechaHoraSalidaImprimible() {
        return circulacion().getCadenaHoraFechaSalida();
    }

    /**
     * Devuelve el número del tren de la circulación
     * @return Número del tren de dicha circulación
     */
    public int numeroTren() {
        return circulacion().trenNumero();
    }

    /**
     * Nombre de la ciudad de origen de la circulación
     * @return Nombre de la ciudad de origen de la circulación
     */
    public String nombreOrigen() {
        return circulacion().ciudadOrigen();
    }

    /**
     * Nombre de la ciudad de destino de la circulación
     * @return Nombre de la ciudad de destino de la circulación
     */
    public String nombreDestino() {
        return circulacion().ciudadDestino();
    }


    /**
     * Identificador de la circulación
     * @return Identificador de la circulación
     */
    @NotNull
    public UUID idCirculacion() {
        return circulacion().id();
    }

    /**
     * Estado de la circulación
     * @return Estado de la circulación
     */
    public EnumCirculacion estado() {
        return circulacion().estado();
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Reserva{"
                + "id=" + id
                + ", usuario=" + usuario
                + ", circulacion=" + circulacion
                + '}';
    }
}
