package dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamWriter;

public abstract class AbstractDAO implements IDAO {

    Logger logger;

    void obtenerLogger() {
        if (logger != null) {
            return;
        }
        logger = LoggerFactory.getLogger(this.getClass());
    }

    abstract protected XMLStreamWriter obtenerXMLStreamWriter();

    abstract protected void guardarArchivo(XMLStreamWriter writer);

    private void cerrarArchivo(XMLStreamWriter writer) {
        try {
            writer.close();
        } catch (Exception e) {
            logger.error("Error al cerrar el archivo", e);
        }
    }


    @Override
    public boolean save() {
        XMLStreamWriter writer = null;
        obtenerLogger();
        logger.info("Guardando archivo...");

        try {
            writer = obtenerXMLStreamWriter();
            guardarArchivo(writer);
        } catch (Exception e) {
            logger.error("Error al volcar el archivo", e);
            return false;
        } finally {
            cerrarArchivo(writer);
            logger.info("Archivo guardado de forma satisfactoria y cerrado correctamente");
        }
        return true;
    }

    abstract protected XMLEventReader obtenerXmlEventReader();

    abstract protected void cargarArchivo(XMLEventReader reader);

    private void cerrarArchivo(XMLEventReader reader) {
        try {
            reader.close();
        } catch (Exception e) {
            logger.error("Error al cerrar el archivo", e);
        }
    }


    @Override
    public boolean load() {
        XMLEventReader reader = null;
        obtenerLogger(); // no hace nada si t_odo está bien, y si no, inicializa logger
        logger.info("Cargando archivo...");

        try {
            reader = obtenerXmlEventReader();
            cargarArchivo(reader);
        } catch (Exception e) {
            logger.error("Error al cargar el archivo", e);
            return false;
        } finally {
            cerrarArchivo(reader);
            logger.info("Archivo cargado de forma satisfactoria");
        }
        return true;
    }

}
