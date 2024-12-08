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
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.*;

import static dao.constantes.TagsXMLReserva.*;

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

    public static DAOReserva getInstance(FachadaDAO fdao) {
        if (instance == null) {
            instance = new DAOReserva(fdao);
        }
        return instance;
    }

    @TestOnly
    public static void main(String[] args) {
        FachadaDAO fdao = FachadaDAO.getInstance();
        fdao.cargaloTodo();
        DAOReserva daoReserva = getInstance(fdao);
        Usuario    usr        = new Usuario(12345678, "Pepe", "Perez@e.e", "hola", 658965874, "eer", LocalDate.now(), true);
        daoReserva.addReserva(usr, new Reserva(UUID.randomUUID(), usr, new Circulacion(UUID.randomUUID(), null, null, null, null, null, null)));


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


    private void addReserva(Usuario usuario, Reserva reserva) {
        comprobarSiExistereservaYAnhadirla(usuario, reserva);
    }

    void addReservaDesdeCirculacion(Usuario usuario, Circulacion circulacion) {
        Reserva reserva = new Reserva(usuario, circulacion);
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

    private Map<Usuario, List<Reserva>> reservas() {
        return reservas;
    }

    List<Reserva> localizarReservasUsuario(Usuario usuario) {
        return this.reservas().get(usuario);
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

        abrirCabeceraArchivoXML(writer, ConstantesGeneral.FICHERO_RESERVA);

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

    //-*-
    //--

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

        String usuarioDNI    = null;
        String circulacionId = null;
        String id            = null;

        while (reader.hasNext()) {

            XMLEvent evento = getNextXmlEvent(reader);
            this.logger.info("Evento: {}", evento);
            if (evento.isStartElement()) {
                this.logger.info("Elemento: {}", evento.asStartElement().getName().getLocalPart());
                StartElement startElement = evento.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case XML_TAG_RESERVA:
                        this.logger.info("Reserva encontrada");
                        break;
                    case XML_TAG_DNI:
                        evento = this.getNextXmlEvent(reader);
                        usuarioDNI = evento.asCharacters().getData();
                        this.logger.info("Usuario DNI: {}", usuarioDNI);
                        break;
                    case XML_TAG_ID_CIRCULACION:
                        evento = this.getNextXmlEvent(reader);
                        circulacionId = evento.asCharacters().getData();
                        this.logger.info("Circulación ID: {}", circulacionId);
                        break;
                    case XML_TAG_ID_RESERVA:
                        evento = this.getNextXmlEvent(reader);
                        id = evento.asCharacters().getData();
                        break;
                    case ConstantesGeneral.FICHERO_RESERVA:
                        this.logger.info("Inicio de archivo");
                        break;
                    default:
                        this.logger.error("Etiqueta no reconocida: {}", startElement.getName().getLocalPart());
                        break;
                }
            }
            if (evento.isEndElement()) {
                this.logger.info("Fin de elemento: {}", evento.asEndElement().getName().getLocalPart());
                if (evento.asEndElement().getName().getLocalPart().equals(XML_TAG_RESERVA)) {

                    if (usuarioDNI == null || circulacionId == null || id == null) {
                        this.logger.error("Error al cargar la reserva: valores nulos");
                        continue;
                    }

                    Usuario usuario;
                    try {
                        usuario = fdao.encontrarUsuarioPorDNI(usuarioDNI);
                    } catch (UsuarioNoEncontradoException e) {
                        this.logger.error("Usuario no encontrado", e);
                        continue;
                    }

                    Circulacion circulacion = fdao.localizarCirculacion(UUID.fromString(circulacionId));

                    UUID reservaId = UUID.fromString(id);

                    Reserva reserva = new Reserva(reservaId, usuario, circulacion);

                    try {
                        this.addReserva(usuario, reserva);
                    } catch (Exception e) {
                        continue;
                    }

                    id = null;
                    circulacionId = null;
                    usuarioDNI = null;
                }
            }
        }

    }
}
//----