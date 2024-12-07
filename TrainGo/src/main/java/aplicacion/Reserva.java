package aplicacion;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.UUID;

public final class Reserva {
    private final UUID        id;
    private final Usuario     usuario;
    private final Circulacion circulacion;
//    private final int asiento;

    public Reserva(Usuario usuario, Circulacion circulacion) {
        this.id = UUID.randomUUID();
        this.usuario = usuario;
        this.circulacion = circulacion;
//            this.asiento = asiento;
    }

    @NotNull
    @Contract(pure = true)
    public BigInteger precio() {
        return circulacion().precio();
    }

    public Circulacion circulacion() {
        return circulacion;
    }

    @Override
    public int hashCode() {
        return id().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Reserva reserva)) {
            return false;
        }
        return id().equals(reserva.id()) || (usuario().equals(reserva.usuario()) && circulacion().equals(reserva.circulacion()));
    }

    public UUID id() {
        return id;
    }

    public Usuario usuario() {
        return usuario;
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
}
