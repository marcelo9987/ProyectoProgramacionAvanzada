package com.marcesoft.traingo.dao;

import com.marcesoft.traingo.aplicacion.Estacion;
import com.marcesoft.traingo.aplicacion.excepciones.LecturaSiguienteEventoException;
import com.marcesoft.traingo.dao.constantes.ConstantesGeneral;
import com.marcesoft.traingo.dao.constantes.TagsXMLEstacion;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import javax.xml.stream.*;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

//--

/**
 * Clase que gestiona el IO y la persistencia de las estaciones
 */
public class DAOEstacion extends AbstractDAO {

    private static volatile DAOEstacion instance = null;
    private final List<Estacion> estaciones;

    private DAOEstacion() {
        super();
        this.estaciones = new ArrayList<>();
        this.obtenerLogger();
    }

    /**
     * Método main para pruebas
     *
     * @param args no se usa
     */
    @TestOnly
    public static void main(String[] args) {
        DAOEstacion dao = new DAOEstacion();
        XMLEventReader xmlEvtRdr_lector = dao.obtenerXmlEventReader();
        dao.cargarArchivo(xmlEvtRdr_lector);

        for (Estacion estacion : dao.estaciones()) {
            System.out.println(estacion);
        }

        dao.addEstacion(new Estacion("Oporto"));
        dao.addEstacion(new Estacion("Mataró"));

        dao.save();
    }

    /**
     * Método que devuelve la lista de estaciones
     * @return Lista de estaciones
     */
    List<Estacion> estaciones() {
        return estaciones;
    }

    /**
     * Método que devuelve la instancia de DAOEstacion
     *
     * @return instancia de DAOEstacion
     */
    public static DAOEstacion getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (DAOEstacion.class) {
            if (instance == null) {
                instance = new DAOEstacion();
            }
        }
        return instance;
    }

    /**
     * Añade una estación a la lista de estaciones
     *
     * @param estacion Estación a añadir
     */
    private void addEstacion(Estacion estacion) {
        if (estacion == null) {
            this.logger.warn("No se puede añadir una estación nula");
            return;
        }

        if (estaciones.contains(estacion)) {
            this.logger.warn("La estación ya existe");
            return;
        }

        estaciones.add(estacion);

    }

    @Override
    protected XMLStreamWriter obtenerXMLStreamWriter() {
        this.obtenerLogger();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter  xmlWriter        = null;
        try {
            xmlWriter = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("datos/estaciones.xml"));
        } catch (FileNotFoundException | XMLStreamException e) {
            this.logger.error("Error al volcar el archivo", e);
        }
        return xmlWriter;
    }

//--

    @Override
    protected void guardarArchivo(@NotNull XMLStreamWriter writer) {
        escribirAperturaCabeceraArchivoXML(writer, ConstantesGeneral.FICHERO_ESTACION);

        for (Estacion estacion : estaciones) {
            try {
                writer.writeStartElement(TagsXMLEstacion.XML_TAG_ESTACION);
                escribirElemento(writer, "nombre", estacion.ciudad());
                writer.writeEndElement();
            } catch (XMLStreamException e) {
                this.logger.error("¡¡CRITICO!! --> Error al escribir la estacion. LA INFORMACIÓN PUEDE ESTAR CORRUPTA", e);
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

    //-*-
    //--

    //----
    @Override
    protected XMLEventReader obtenerXmlEventReader() {
        this.obtenerLogger();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlReader;
        try {
            xmlReader = xmlInputFactory.createXMLEventReader(new FileInputStream("datos/estaciones.xml"));
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new LecturaSiguienteEventoException();
        }
        return xmlReader;
    }

    @Override
    protected void cargarArchivo(@NotNull XMLEventReader reader) {
        Estacion estacion;
        String nombre = "err";

        while (reader.hasNext()) {
            XMLEvent evento = getNextXmlEvent(reader);
            this.logger.trace("Evento: {}", evento);
            if (evento.isStartElement()) {
                StartElement elementoInicio = evento.asStartElement();
                try {
                    switch (elementoInicio.getName().getLocalPart()) {
                        case TagsXMLEstacion.XML_TAG_ESTACION:
                            System.out.println("Inicio de estacion");
                            break;
                        case "nombre":
                            evento = this.getNextXmlEvent(reader);
                            nombre = evento.asCharacters().getData();
                            System.out.printf("Nombre: %s\n", nombre);
                            break;
                    }
                } catch (IllegalStateException e) {
                    logger.error("Error al leer el archivo", e);
                }
            }
            if (evento.isEndElement()) {
                if (evento.asEndElement().getName().getLocalPart().equals(TagsXMLEstacion.XML_TAG_ESTACION)) {
                    System.out.println("Fin de estacion");
                    estacion = new Estacion(nombre);
                    estaciones.add(estacion);
                    System.out.printf("Estacion añadida: %s\n", estacion);
                }
            }
        }
    }


    /**
     * Busca una estación por su nombre
     * @param nombreEstacion Nombre de la estación a buscar
     * @return Estación encontrada o null si no se encuentra
     */
    Estacion buscaEstacionPorNombre(@NonNls String nombreEstacion) {
        return estaciones.stream().filter(e -> e.ciudad().equals(nombreEstacion)).findFirst().orElse(null);
    }
}