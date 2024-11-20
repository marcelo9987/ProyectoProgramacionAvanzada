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

public class DAOTren implements IDAO {

    private static DAOTren instance = null;

    List<Tren> trenes = null;

    private DAOTren() {
        this.trenes = new ArrayList<>();
    }


    public static DAOTren getInstance() {
        if (instance == null) {
            instance = new DAOTren();
        }
        return instance;
    }

    public void addTren(Tren tren) {
        LoggerFactory.getLogger(DAOTren.class).trace("Añadiendo tren: " + tren);
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
    public Boolean save() {
        BufferedWriter writer = null;

        try {
            FileWriter file = new FileWriter("trenes.txt");
            writer = new BufferedWriter(file);
            for (Tren tren : trenes) {
                writer.write(tren.toString());
                writer.newLine();
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(DAOTren.class).error("Error al volcar el archivo");
            return false;
        }

        try {
            writer.close();
        } catch (Exception e) {
            LoggerFactory.getLogger(DAOTren.class).error("Error al cerrar el archivo, no se guardaron los cambios");
            return false;
        }

        return true;
    }

    @Override
    public Boolean load() {
        BufferedReader br_trainReader = null;
        try {
            br_trainReader = new BufferedReader(new FileReader("trenes.txt"));
            String line = br_trainReader.readLine();
            while (line != null) {
                String[] parts = line.split(",");
                trenes.add(new Tren(UUID.fromString(parts[0]), Integer.parseInt(parts[1])));
                line = br_trainReader.readLine();
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(DAOTren.class).error("Error al cargar el archivo");
            return false;
        }
        return true;
    }

}
