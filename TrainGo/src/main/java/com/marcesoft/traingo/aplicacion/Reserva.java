package com.marcesoft.traingo.aplicacion;

import com.marcesoft.traingo.aplicacion.enums.EnumCirculacion;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Clase Reserva
 * @param id          Identificador de la reserva
 * @param usuario     Usuario que ha realizado la reserva
 * @param circulacion Circulación reservada
 */
public record Reserva(@org.hibernate.validator.constraints.UUID UUID id, Usuario usuario, Circulacion circulacion) {

    /**
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
     * @return Fecha y hora de salida de la circulación en formato imprimible
     */
    @NotNull
    public String fechaHoraSalidaImprimible() {
        return circulacion().getCadenaHoraFechaSalida();
    }

    /**
     * @return Número de tren en la circulación
     */
    public int numeroTren() {
        return circulacion().trenNumero();
    }

    /**
     * @return Nombre de la ciudad de origen de la circulación
     */
    public String nombreOrigen() {
        return circulacion().ciudadOrigen();
    }

    /**
     * @return Nombre de la ciudad de destino de la circulación
     */
    public String nombreDestino() {
        return circulacion().ciudadDestino();
    }


    /**
     * @return Identificador de la circulación
     */
    @NotNull
    public UUID idCirculacion() {
        return circulacion().id();
    }

    /**
     * @return Estado de la circulación
     */
    public EnumCirculacion estado() {
        return circulacion().estado();
    }
}
