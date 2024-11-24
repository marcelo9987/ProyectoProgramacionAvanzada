package dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public abstract class AbstractDAO implements IDAO {

    Logger logger;

    void obtenerLogger() {
        if (logger != null) {
            return;
        }
        logger = LoggerFactory.getLogger(this.getClass());
    }

    abstract protected BufferedWriter obtenerFileWriter();

    abstract protected void guardarArchivo(BufferedWriter writer);

    void cerrarArchivo(BufferedWriter writer) {
        try {
            writer.close();
        } catch (Exception e) {
            logger.error("Error al cerrar el archivo", e);
        }
    }

    @Override
    public Boolean save() {
        BufferedWriter writer = null;
        obtenerLogger();
        logger.info("Guardando archivo...");

        try {
            writer = obtenerFileWriter();
            guardarArchivo(writer);
        } catch (Exception e) {
            logger.error("Error al volcar el archivo", e);
            return false;
        } finally {
            cerrarArchivo(writer);
            logger.info("Archivo guardado de forma satisfactoria");
        }
        return true;
    }

    abstract protected BufferedReader obtenerFileReader();

    abstract protected void cargarArchivo(BufferedReader reader);

    void cerrarArchivo(BufferedReader reader) {
        try {
            reader.close();
        } catch (Exception e) {
            logger.error("Error al cerrar el archivo", e);
        }
    }

    @Override
    public Boolean load() {
        BufferedReader reader = null;
        obtenerLogger(); // no hace nada si t_odo está bien, y si no, inicializa logger
        logger.info("Cargando archivo...");

        try {
            reader = obtenerFileReader();
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
