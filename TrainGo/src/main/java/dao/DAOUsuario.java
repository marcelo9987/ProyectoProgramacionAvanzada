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
        BufferedWriter writer;

        try {
            FileWriter file = new FileWriter("usuarios.txt");
            writer = new BufferedWriter(file);
        } catch (Exception e) {
            this.logger.error("Error al volcar el archivo", e);
            return null;
        }

        return writer;

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
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("usuarios.txt"));
        } catch (Exception e) {
            this.logger.error("Error al leer el archivo", e);
            return null;
        }

        return reader;
    }

    @Override
    protected void cargarArchivo(BufferedReader reader) {
        String linea;
        try {
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");

                Usuario usr = new Usuario(Integer.parseInt(partes[0].substring(8)), partes[1], partes[2], partes[3], Integer.parseInt(partes[4]), partes[5], new Date(partes[6].split("}")[0]), false);
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


    /**
     * Mét.odo que autentica a un usuario
     *
     * @param email          Correo del usuario
     * @param hashedPassword Contraseña del usuario
     * @return true si el usuario es autenticado, false en caso contrario
     */
    public boolean autenticar(String email, String hashedPassword) {
        //logar la contraseña descifrada :: cifrada (DEBUG)
//        this.logger.debug("Contraseña cifrada: {} -> descifrada: {}", hashedPassword, util.criptograficos.descifrar(hashedPassword));

        comprobarHayUsuariosEnOrigen();
        // Había pensado en comprobar si el correo existe, pero no sé si es muy eficiente, ya que se recorrería la lista dos veces.
        if (this.usuarios.stream().anyMatch(usr ->/* usr.correo().equals(email) &&*/ usr.contrasenha().equals(hashedPassword))) {
            this.logger.info("Usuario autenticado: {}", email);
            return true;
        }
        this.logger.warn("Usuario no autenticado: {}", email);
        return false;
    }

    /**
     * Mét.odo que obtiene un usuario
     *
     * @param email Correo del usuario
     * @return Usuario si existe, null en caso contrario
     */
    public Usuario encontrarUsuarioPorEmail(String email) {
        if (comprobarHayUsuariosEnOrigen()) {
            return null;
        }
        Usuario usr = this.usuarios.stream().filter(u -> u.correo().equals(email)).findFirst().orElse(null);
        if (usr != null) {
            this.logger.info("Usuario encontrado: {}", email);
        }
        else {
            this.logger.warn("Usuario no encontrado: {}", email);
        }
        return usr;
    }

    private boolean comprobarHayUsuariosEnOrigen() {
        if (usuarios.isEmpty()) {
            this.logger.warn("No hay usuarios registrados");
            return true;
        }
        return false;
    }
}
