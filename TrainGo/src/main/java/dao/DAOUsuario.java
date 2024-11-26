package dao;

import modelo.Usuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAOUsuario extends AbstractDAO {

    private static DAOUsuario instance = null;
    List<Usuario> usuarios;

    private DAOUsuario() {
        this.obtenerLogger();
        this.usuarios = new ArrayList<>();
    }

    public static DAOUsuario getInstance() {
        if (instance == null) {
            instance = new DAOUsuario();
        }
        return instance;
    }

    @Override
    protected BufferedWriter obtenerFileWriter() {
        this.obtenerLogger();
        BufferedWriter mrBtheGentlemanWriter;

        try {
            FileWriter file = new FileWriter("usuarios.txt");
            mrBtheGentlemanWriter = new BufferedWriter(file);
        } catch (Exception e) {
            this.logger.error("Error al volcar el archivo", e);
            return null;
        }

        return mrBtheGentlemanWriter;

    }

    @Override
    protected void guardarArchivo(BufferedWriter writer) {
        for (Usuario usuario : usuarios) {
            try {
                writer.write(usuario.printReadyString());
                writer.newLine();
            } catch (Exception e) {
                this.logger.error("Error al escribir en el archivo", e);
            }
        }
    }

    @Override
    protected BufferedReader obtenerFileReader() {
        this.obtenerLogger();
        BufferedReader mrBtheGentlemanReader;

        try {
            mrBtheGentlemanReader = new BufferedReader(new FileReader("usuarios.txt"));
        } catch (Exception e) {
            this.logger.error("Error al leer el archivo", e);
            return null;
        }

        return mrBtheGentlemanReader;
    }

    @Override
    protected void cargarArchivo(BufferedReader reader) {
        String linea;
        try {
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");

                Usuario usr = new Usuario(Integer.parseInt(partes[0].substring(8)), partes[1], partes[2], partes[3], Integer.parseInt(partes[4]), partes[5], new Date(partes[6].split("}")[0]));
                if (usuarios.contains(usr)) {
                    this.logger.warn("Usuario duplicado: {}. No se cargará", usr);
                    continue;
                }
                usuarios.add(usr);
                this.logger.debug("Usuario cargado: {}", usr);
            }
        } catch (Exception e) {
            this.logger.error("Error al leer el archivo", e);
        }
    }

    public void addUser(Usuario usr) {
        this.logger.trace("Añadiendo usuario: {}", usr);
        usuarios.add(usr);
    }
}
