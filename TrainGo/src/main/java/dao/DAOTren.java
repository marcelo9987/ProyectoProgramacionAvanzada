package dao;

import aplicacion.Tren;
import dao.constantes.ConstantesGeneral;
import dao.constantes.TagsXMLTren;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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


    /**
     * Método que devuelve la instancia de DAOTren
     *
     * @return instancia de DAOTren
     */
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
        } catch (FileNotFoundException | XMLStreamException e) {
            this.logger.error("Error al solicitar P sobre el archivo", e);
            return null;
        }
        this.logger.trace("XMLStreamWriter obtenido. Todo listo para guardar el archivo...");
        return writer;
    }

    @Override
    protected void guardarArchivo(XMLStreamWriter writer) {
        this.logger.trace("Guardando contenido en el archivo...");

        escribirAperturaCabeceraArchivoXML(writer, ConstantesGeneral.FICHERO_TREN);

        for (Tren tren : trenes) {
            try {
                writer.writeStartElement(TagsXMLTren.XML_TAG_TREN);

                escribirElemento(writer, TagsXMLTren.XML_TAG_ID, tren.id().toString());

                escribirElemento(writer, TagsXMLTren.XML_TAG_NUM, String.valueOf(tren.num()));

                writer.writeEndElement();
            } catch (XMLStreamException e) {
                this.logger.error("¡¡CRITICO!! --> Error al escribir el tren. LA INFORMACIÓN PUEDE ESTAR CORRUPTA", e);

            }
        }

        try {
            writer.writeEndElement();
            writer.writeEndDocument();
        } catch (XMLStreamException e) {
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
        } catch (FileNotFoundException | XMLStreamException e) {
            this.logger.error("Error al solicitar P sobre el archivo. Puede que la ruta sea incorrecta o el archivo no exista", e);
        }
        this.logger.trace("XMLEventReader obtenido. Todo listo para cargar el archivo...");
        return reader;
    }

    @Override
    protected void cargarArchivo(@NotNull XMLEventReader reader) {
        this.logger.trace("Cargando contenido del archivo...");

        String id = null;
        Integer num = null;

        while (reader.hasNext()) {
            XMLEvent evento = this.getNextXmlEvent(reader);

            if (evento.isStartElement()) {
                try {
                    switch (evento.asStartElement().getName().getLocalPart()) {
                        case TagsXMLTren.XML_TAG_TREN:
                            this.logger.trace("Inicio de tren");
                            break;
                        case TagsXMLTren.XML_TAG_ID:
                            evento = reader.nextEvent();
                            id = evento.asCharacters().getData();
                            break;
                        case TagsXMLTren.XML_TAG_NUM:
                            evento = reader.nextEvent();
                            num = Integer.parseInt(evento.asCharacters().getData());
                            break;
                    }
                } catch (NumberFormatException | XMLStreamException e) {
                    this.logger.error("Error al leer el archivo. ¿Está bien formado?", e);
                }
            }

            if (evento.isEndElement() && evento.asEndElement().getName().getLocalPart().equals(TagsXMLTren.XML_TAG_TREN)) {
                this.logger.trace("Fin de tren");
                if (_comprobarVariablesTrenNoNulas(id, num)) {
                    this.logger.warn("No se ha podido cargar el tren (Parámetro de entrada incorrecto) . ¿Está bien formado el archivo?");
                    continue;
                }
//                    tren = new Tren(UUID.fromString(id), num.intValue());
                trenes.add(new Tren(UUID.fromString(id), num));
            }

        }


    }

    private static boolean _comprobarVariablesTrenNoNulas(String id, Integer num) {
        return id == null || num == null;
    }

    @Nullable
    Tren localizarTren(String trenId) {
        for (Tren tren : trenes) {
            if (tren.id().toString().equals(trenId)) {
                return tren;
            }
        }
        return null;
    }
}
