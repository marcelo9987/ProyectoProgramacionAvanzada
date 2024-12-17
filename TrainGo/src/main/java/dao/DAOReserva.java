package dao;

import aplicacion.Circulacion;
import aplicacion.Reserva;
import aplicacion.Usuario;
import aplicacion.excepciones.LecturaSiguienteEventoException;
import aplicacion.excepciones.SituacionDeRutasInesperadaException;
import aplicacion.excepciones.UsuarioNoEncontradoException;
import dao.constantes.ConstantesGeneral;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

import static dao.constantes.TagsXMLReserva.*;

/**
 * Clase que gestiona el IO y la persistencia de las reservas
 */
public class DAOReserva extends AbstractDAO {
    private static DAOReserva                  instance = null;
    private final  FachadaDAO                  fdao;
    private final  Map<Usuario, List<Reserva>> reservas;

    private DAOReserva(FachadaDAO fdao) {
        super();
        this.fdao = fdao;
        this.reservas = new HashMap<>();
        this.obtenerLogger();
    }

    /**
     * Método main para pruebas
     *
     * @param args no se usa
     */
    @TestOnly
    public static void main(String[] args) {
        FachadaDAO fdao = FachadaDAO.getInstance();
        fdao.cargarTodosLosDatosDeFicheros();
        DAOReserva daoReserva = getInstance(fdao);


        XMLEventReader reader = daoReserva.obtenerXmlEventReader();

        daoReserva.cargarArchivo(reader);
        for (Map.Entry<Usuario, List<Reserva>> entry : daoReserva.reservas().entrySet()) {
            for (Reserva reserva : entry.getValue()) {
                System.out.println(reserva);
            }
        }


        try {
            reader.close();
        } catch (XMLStreamException e) {
            System.exit(0);
        }


        XMLStreamWriter writer = daoReserva.obtenerXMLStreamWriter();
        daoReserva.guardarArchivo(writer);
    }

    /**
     * Método que devuelve la instancia de DAOReserva
     *
     * @param fdao FachadaDAO
     * @return instancia de DAOReserva
     */
    public static DAOReserva getInstance(FachadaDAO fdao) {
        if (instance == null) {
            instance = new DAOReserva(fdao);
        }
        return instance;
    }

    private Map<Usuario, List<Reserva>> reservas() {
        return reservas;
    }

    @Override
    protected XMLStreamWriter obtenerXMLStreamWriter() {

        this.obtenerLogger();

        XMLStreamWriter writer;

        try {
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            writer = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("reservas.xml"));
        } catch (FileNotFoundException | XMLStreamException e) {
            this.logger.error("Error al volcar el archivo", e);
            throw new SituacionDeRutasInesperadaException("Error al obtener punteros de escritura");
        }

        return writer;
    }

    @Override
    protected void guardarArchivo(@NotNull XMLStreamWriter writer) {

        escribirAperturaCabeceraArchivoXML(writer, ConstantesGeneral.FICHERO_RESERVA);

        try {

            for (Map.Entry<Usuario, List<Reserva>> entry : this.reservas().entrySet()) {
                for (Reserva reserva : entry.getValue()) {

                    writer.writeStartElement(XML_TAG_RESERVA);

                    escribirElemento(writer, XML_TAG_DNI, String.valueOf(entry.getKey().dni()));

                    escribirElemento(writer, XML_TAG_ID_CIRCULACION, reserva.idCirculacion().toString());

                    escribirElemento(writer, XML_TAG_ID_RESERVA, reserva.id().toString());

                    writer.writeEndElement();
                }
            }

            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            this.logger.error("Error al guardar el archivo", e);
            throw new SituacionDeRutasInesperadaException("Error al guardar el archivo");
        }

    }

    @Override
    protected XMLEventReader obtenerXmlEventReader() {
        this.obtenerLogger();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader  xmlReader;
        try {
            xmlReader = xmlInputFactory.createXMLEventReader(new FileInputStream("reservas.xml"));
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new LecturaSiguienteEventoException();
        }
        this.logger.trace("XMLEventReader obtenido. Todo listo para cargar el archivo...");
        return xmlReader;
    }

    @Override
    protected void cargarArchivo(@NotNull XMLEventReader reader) {

        String usuarioDNI = null, circulacionId = null, id = null;

        while (reader.hasNext()) {
            XMLEvent evento = getNextXmlEvent(reader);
            if (evento.isStartElement()) {
                this.logger.trace("Elemento: {}", evento.asStartElement().getName().getLocalPart());
                switch (evento.asStartElement().getName().getLocalPart()) {
                    case XML_TAG_DNI -> usuarioDNI = _procesarCadenaTexto(reader);
                    case XML_TAG_ID_CIRCULACION -> circulacionId = _procesarCadenaTexto(reader);
                    case XML_TAG_ID_RESERVA -> id = _procesarCadenaTexto(reader);
                    case ConstantesGeneral.FICHERO_RESERVA, XML_TAG_RESERVA -> this.logger.debug("Inicio de archivo | Reserva");
                    default -> this.logger.error("Etiqueta no reconocida: {}", evento.asStartElement().getName().getLocalPart());
                }
            }
            if (_esFinalDeEtiqueta(evento)) {
                boolean saltar;

                saltar = _comprobarParametrosNuevaReserva(usuarioDNI, circulacionId, id);


                recordSaltarOGuardarReserva reservaMasticada = _procesar(usuarioDNI, saltar, circulacionId, id);

                if (reservaMasticada.saltar()) {
                    continue;
                }

                try {
                    this.addReserva(reservaMasticada.usuario(), reservaMasticada.reserva());
                } catch (IllegalArgumentException e) {
                    this.logger.error("Error al añadir la reserva", e);
                }

                id = null;
                circulacionId = null;
                usuarioDNI = null;
            }
        }

    }

    private String _procesarCadenaTexto(XMLEventReader reader) {
        XMLEvent evento = this.getNextXmlEvent(reader);
        String   campoAusar;
        campoAusar = evento.asCharacters().getData();
        return campoAusar;
    }

    private static boolean _esFinalDeEtiqueta(@NotNull XMLEvent evento) {
        return evento.isEndElement() && evento.asEndElement().getName().getLocalPart().equals(XML_TAG_RESERVA);
    }

    //-*-
    //--

    private boolean _comprobarParametrosNuevaReserva(String usuarioDNI, String circulacionId, String id) {
        if (usuarioDNI == null || circulacionId == null || id == null) {
            this.logger.error("Error al cargar la reserva: valores nulos");
            return true;
        }
        return false;
    }

    @NotNull
    private recordSaltarOGuardarReserva _procesar(String usuarioDNI, boolean saltar, String circulacionId, String id) {
        if (saltar) {
            return new recordSaltarOGuardarReserva(true, null, null);
        }
        Usuario usuario = null;
        try {
            usuario = fdao.encontrarUsuarioPorDNI(usuarioDNI);
        } catch (UsuarioNoEncontradoException e) {
            this.logger.error("Usuario no encontrado", e);
            saltar = true;
        }

        Circulacion circulacion = fdao.localizarCirculacion(UUID.fromString(circulacionId));

        UUID reservaId = UUID.fromString(id);

        Reserva reserva = new Reserva(reservaId, usuario, circulacion);
        return new recordSaltarOGuardarReserva(saltar, usuario, reserva);
    }

    private void addReserva(Usuario usuario, Reserva reserva) {
        comprobarSiExistereservaYAnhadirla(usuario, reserva);
    }

    private void comprobarSiExistereservaYAnhadirla(Usuario usuario, Reserva reserva) {
        if (this.reservas().containsKey(usuario)) {
            if (this.reservas().get(usuario).contains(reserva)) {
                logger.info("La reserva ya existe {}", reserva);
                throw new IllegalArgumentException("La reserva ya existe");
            }
            else {
                this.reservas().get(usuario).add(reserva);
            }
        }
        else {
            List<Reserva> reservas = new ArrayList<>();
            reservas.add(reserva);
            this.reservas().put(usuario, reservas);
        }
    }

    void addReservaDesdeCirculacion(Usuario usuario, Circulacion circulacion) {
        Reserva reserva = new Reserva(usuario, circulacion);
        comprobarSiExistereservaYAnhadirla(usuario, reserva);
    }

    List<Reserva> localizarReservasUsuario(Usuario usuario) {
        return this.reservas().get(usuario);
    }

    private record recordSaltarOGuardarReserva(boolean saltar, Usuario usuario, Reserva reserva) {
    }
}
//----