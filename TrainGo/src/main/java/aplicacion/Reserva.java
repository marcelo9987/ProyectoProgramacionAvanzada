package aplicacion;

import aplicacion.enums.EnumCirculacion;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Reserva(@org.hibernate.validator.constraints.UUID UUID id, Usuario usuario, Circulacion circulacion) {

//    public Reserva(UUID id, Usuario usuario, Circulacion circulacion){
//        this.id = id;
//        this.usuario = usuario;
//        this.circulacion = circulacion;
//    }

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




    @NotNull
    public String fechaHoraSalidaImprimible() {
        return circulacion().getCadenaHoraFechaSalida();
    }

    public int numeroTren() {
        return circulacion().trenNumero();
    }

    public String nombreOrigen() {
        return circulacion().ciudadOrigen();
    }

    public String nombreDestino() {
        return circulacion().ciudadDestino();
    }


    @NotNull
    public UUID idCirculacion() {
        return circulacion().id();
    }

    public EnumCirculacion estado() {
        return circulacion().estado();
    }
}
