package dao;

import aplicacion.Estacion;
import aplicacion.excepciones.LecturaSiguienteEventoException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.*;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class DAOEstacion extends AbstractDAO {
    private static DAOEstacion instance = null;
    private final List<Estacion> estaciones;

    private DAOEstacion() {
        super();
        this.estaciones = new ArrayList<>();
        this.obtenerLogger();
    }

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


    public static DAOEstacion getInstance() {
        if (instance == null) {
            instance = new DAOEstacion();
        }
        return instance;
    }

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
    protected XMLEventReader obtenerXmlEventReader() {
        this.obtenerLogger();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlReader = null;
        try {
            xmlReader = xmlInputFactory.createXMLEventReader(new FileInputStream("estaciones.xml"));
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
            if (evento.isStartElement()) {
                StartElement elementoInicio = evento.asStartElement();
                try {
                    switch (elementoInicio.getName().getLocalPart()) {
                        case "estacion":
                            System.out.println("Inicio de estacion");
                            break;
                        case "nombre":
                            evento = reader.nextEvent();
                            nombre = evento.asCharacters().getData();
                            System.out.printf("Nombre: %s\n", nombre);
                            break;
                    }
                } catch (Exception e) {
                    //super.logger.error("Error al leer el archivo", e);
                }
            }
            if (evento.isEndElement()) {
                if (evento.asEndElement().getName().getLocalPart().equals("estacion")) {
                    System.out.println("Fin de estacion");
                    estacion = new Estacion(nombre);
                    estaciones.add(estacion);
                    System.out.printf("Estacion añadida: %s\n", estacion);
                }
            }
        }
    }

    @Override
    protected void guardarArchivo(XMLStreamWriter writer) {
        try {
            writer.writeStartDocument();
            writer.writeStartElement("estaciones");
        } catch (Exception e) {
            this.logger.error("Error al escribir la cabecera, es posible que el archivo haya dejado de existir (o el acceso a mutex haya sido revocado)", e);
        }

        for (Estacion estacion : estaciones) {
            try {
                writer.writeStartElement("estacion");
                writer.writeStartElement("nombre");
                writer.writeCharacters(estacion.ciudad());
                writer.writeEndElement();
                writer.writeEndElement();
            } catch (Exception e) {
                this.logger.error("¡¡CRITICO!! --> Error al escribir la estacion. LA INFORMACIÓN PUEDE ESTAR CORRUPTA", e);
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
    protected XMLStreamWriter obtenerXMLStreamWriter() {
        this.obtenerLogger();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = null;
        try {
            xmlWriter = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("estaciones.xml"));
        } catch (Exception e) {
            this.logger.error("Error al volcar el archivo", e);
        }
        return xmlWriter;
    }

    List<Estacion> estaciones() {
        return estaciones;
    }

    public Estacion buscaEstacionPorNombre(@NonNls String nombreEstacion) {
        return estaciones.stream().filter(e -> e.ciudad().equals(nombreEstacion)).findFirst().orElse(null);
    }
}