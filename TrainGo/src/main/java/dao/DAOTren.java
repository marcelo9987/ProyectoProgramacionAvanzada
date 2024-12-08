package dao;

import aplicacion.Tren;
import dao.constantes.ConstantesGeneral;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DAOTren
 *
 * @version 2.0 20/11/2024
 * @apiNote Clase que extiende AbstractDAO y se encarga de gestionar los trenes
 * @implNote Se encarga de gestionar los trenes y su persistencia en un archivo XML
 */
public class DAOTren extends AbstractDAO {

    // Logger (heredado de AbstractDAO)
    private static DAOTren instance = null;

    private final List<Tren> trenes;

    private DAOTren() {
        super();
        this.obtenerLogger();
        this.trenes = new ArrayList<>();
    }


    public static DAOTren getInstance() {
        if (instance == null) {
            instance = new DAOTren();
        }
        return instance;
    }

    @Nullable
    @Override
    protected XMLStreamWriter obtenerXMLStreamWriter() {
        XMLStreamWriter writer;
        try {
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            writer = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("trenes.xml"));
        } catch (Exception e) {
            this.logger.error("Error al solicitar P sobre el archivo", e);
            return null;
        }
        this.logger.trace("XMLStreamWriter obtenido. Todo listo para guardar el archivo...");
        return writer;
    }

    @Override
    protected void guardarArchivo(XMLStreamWriter writer) {
        this.logger.trace("Guardando contenido en el archivo...");

        abrirCabeceraArchivoXML(writer, ConstantesGeneral.FICHERO_TREN);

        for (Tren tren : trenes) {
            try {
                writer.writeStartElement("tren");

                escribirElemento(writer, "id", tren.id().toString());

                escribirElemento(writer, "num", String.valueOf(tren.num()));

                writer.writeEndElement();
            } catch (Exception e) {
                this.logger.error("¡¡CRITICO!! --> Error al escribir el tren. LA INFORMACIÓN PUEDE ESTAR CORRUPTA", e);

            }
        }

        try {
            writer.writeEndElement();
            writer.writeEndDocument();
        } catch (Exception e) {
            this.logger.error("¡¡CRITICO!! --> Error al escribir el final del documento. LA INFORMACIÓN PUEDE ESTAR CORRUPTA", e);
        }

        this.logger.trace("Contenido guardado en el archivo con éxito!");
    }


    @Override
    protected XMLEventReader obtenerXmlEventReader() {

        this.obtenerLogger(); // esta llamada es gratis,
        // ya que logger ya está inicializado, y si no lo está, se inicializa

        XMLEventReader reader = null;
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            reader = xmlInputFactory.createXMLEventReader(new FileInputStream("trenes.xml"));
        } catch (Exception e) {
            this.logger.error("Error al solicitar P sobre el archivo. Puede que la ruta sea incorrecta o el archivo no exista", e);
        }
        this.logger.trace("XMLEventReader obtenido. Todo listo para cargar el archivo...");
        return reader;
    }

    @Override
    protected void cargarArchivo(@NotNull XMLEventReader reader) {
        this.obtenerLogger(); // Esta llamada es gratis. Si ya existe, no hace nada, y si no, inicializa logger

        this.logger.trace("Cargando contenido del archivo...");

        Tren tren;
        String id = null;
        Integer num = null;

        while (reader.hasNext()) {
            XMLEvent evento;
            try {
                evento = reader.nextEvent();
            } catch (Exception e) {
                this.logger.error("Error al leer el evento. ¿Está bien formado el archivo?", e);
                continue;
            }

            if (evento.isStartElement()) {
                try {
                    switch (evento.asStartElement().getName().getLocalPart()) {
                        case "tren":
                            this.logger.trace("Inicio de tren");
                            break;
                        case "id":
                            evento = reader.nextEvent();
                            id = evento.asCharacters().getData();
                            break;
                        case "num":
                            evento = reader.nextEvent();
                            num = Integer.parseInt(evento.asCharacters().getData());
                            break;
                    }
                } catch (Exception e) {
                    this.logger.error("Error al leer el archivo. ¿Está bien formado?", e);
                }
            }

            if (evento.isEndElement()) {
                if (evento.asEndElement().getName().getLocalPart().equals("tren")) {
                    this.logger.trace("Fin de tren");
                    if (id == null || num == null) {
                        this.logger.warn("No se ha podido cargar el tren (Parámetro de entrada incorrecto) . ¿Está bien formado el archivo?");
                        continue;
                    }
                    tren = new Tren(UUID.fromString(id), num.intValue());
                    trenes.add(tren);
                }
            }
        }


    }

    @Nullable
    public Tren localizarTren(String trenId) {
        for (Tren tren : trenes) {
            if (tren.id().toString().equals(trenId)) {
                return tren;
            }
        }
        return null;
    }
}
