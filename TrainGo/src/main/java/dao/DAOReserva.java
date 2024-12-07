package dao;

import aplicacion.Reserva;
import aplicacion.Usuario;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAOReserva extends AbstractDAO {
    private static DAOReserva instance = null;

    private final Map<Usuario, List<Reserva>> reservas;

    private DAOReserva() {
        super();
        this.reservas = new HashMap<>();
        this.obtenerLogger();
    }

    public static DAOReserva getInstance() {
        if (instance == null) {
            instance = new DAOReserva();
        }
        return instance;
    }

    public void addReserva(Usuario usuario, Reserva reserva) {
        if (this.reservas.containsKey(usuario)) {
            if (this.reservas.get(usuario).contains(reserva)) {
                logger.error("La reserva ya existe {}", reserva);
                throw new IllegalArgumentException("La reserva ya existe");
            }
            else {
                this.reservas.get(usuario).add(reserva);
            }
        }
        else {
            List<Reserva> reservas = new ArrayList<>();
            reservas.add(reserva);
            this.reservas.put(usuario, reservas);
        }
    }

    public List<Reserva> localizarReservasUsuario(Usuario usuario) {
        return this.reservas.get(usuario);
    }

    @Override
    protected XMLStreamWriter obtenerXMLStreamWriter() {
        return null;
    }

    @Override
    protected void guardarArchivo(XMLStreamWriter writer) {

    }

    @Override
    protected XMLEventReader obtenerXmlEventReader() {
        return null;
    }

    @Override
    protected void cargarArchivo(XMLEventReader reader) {

    }
}
