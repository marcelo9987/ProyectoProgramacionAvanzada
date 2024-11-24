package dao;
/**
 * DAOTren
 *
 * @apiNote Clase que implementa la interfaz IDAO y se encarga de gestionar los trenes
 * @version 1.0 20/11/2024
 * @implNote Se encarga de gestionar los a
 */

import modelo.Tren;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DAOTren extends AbstractDAO implements IDAO {

    // Logger (heredado de AbstractDAO)
    private static DAOTren instance = null;

    List<Tren> trenes = null;

    private DAOTren() {
        this.obtenerLogger();
        this.trenes = new ArrayList<>();
    }


    public static DAOTren getInstance() {
        if (instance == null) {
            instance = new DAOTren();
        }
        return instance;
    }

    public void addTren(Tren tren) {

        LoggerFactory.getLogger(DAOTren.class).trace("Añadiendo tren: {}", tren);
        trenes.add(tren);
    }

    public List<Tren> getTrenes() {
        return trenes;
    }

    public void deleteTren(Tren tren) {
        trenes.remove(tren);
    }

    public void updateTren(Tren tren) {
        trenes.set(trenes.indexOf(tren), tren);
    }

    @Override
    protected BufferedWriter obtenerFileWriter() { //todo: NORMALÍZAME
        BufferedWriter writer = null;
        try {
            FileWriter file = new FileWriter("trenes.txt");
            writer = new BufferedWriter(file);
        } catch (Exception e) {
            LoggerFactory.getLogger(DAOTren.class).error("Error al volcar el archivo");
            return null;
        }
        return writer;
    }

    @Override
    protected void guardarArchivo(BufferedWriter writer) {
        for (Tren tren : trenes) {
            try {
                writer.write(tren.toString());
            } catch (Exception e) {
                this.logger.error("Error al guardar el archivo", e);
            }
        }
        this.logger.trace("Contenido guardado en el archivo...");
    }


    @Override
    protected BufferedReader obtenerFileReader() {
        this.obtenerLogger(); // esta llamada es gratis,
        // ya que logger ya está inicializado, y si no lo está, se inicializa

        BufferedReader reader = null;
        try {
            FileReader file = new FileReader("trenes.txt");
            reader = new BufferedReader(file);
        } catch (Exception e) {
            this.logger.error("Error al leer el archivo", e);
            return null;
        }
        return reader;
    }

    @Override
    protected void cargarArchivo(BufferedReader reader) {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                UUID id = UUID.fromString(parts[0].split("id=")[1]);
                int num = Integer.parseInt(parts[1].split("num=")[1].split("]")[0]);
                Tren tren = new Tren(id, num);
                trenes.add(tren);

                // DEBUG
                this.logger.debug("Tren cargado: {}", tren);

            }
        } catch (Exception e) {
            this.logger.error("Error al cargar el archivo", e);
        }
    }
}
