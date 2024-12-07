package dao;

import aplicacion.*;
import aplicacion.excepciones.UsuarioNoEncontradoException;
import org.jetbrains.annotations.TestOnly;

import java.time.LocalDate;
import java.util.List;

public class FachadaDAO {
    private static FachadaDAO instance = null; // Singleton pattern

    private final DAOTren     daoTren;
    private final DAOUsuario  daoUsuario;
    private final DAOEstacion daoEstacion;
    private final DAORuta     daoRuta;
    private final DAOCirculacion daoCirculacion;
    private final DAOReserva  daoReserva;

    // Constructor

    private FachadaDAO() {
        super();

        daoTren = DAOTren.getInstance();

        daoUsuario = DAOUsuario.getInstance();

        daoEstacion = DAOEstacion.getInstance();

        daoRuta = DAORuta.getInstance(this);

        daoCirculacion = DAOCirculacion.getInstance(this);

        daoReserva = DAOReserva.getInstance();
    }

    /**
     * Método que devuelve la instancia de la clase FachadaDAO
     *
     * @return instance Instancia de la clase FachadaDAO. Si no existe, la crea
     */
    public static FachadaDAO getInstance()// Singleton
    {
        if (instance == null) {
            instance = new FachadaDAO();
        }
        return instance;
    }

    // BUSINESS LOGIC

    // CRUD Tren (Crear, Leer, Actualizar, Borrar)

    public void addTren(Tren tren) {
        this.daoTren.addTren(tren);
    }


    public void loadTren() {
        this.daoTren.load();
    }


    public void updateTren(Tren tren) {
        this.daoTren.updateTren(tren);
    }

    public void saveTren() {
        this.daoTren.save();
    }


    public void deleteTren(Tren tren) {
        this.daoTren.deleteTren(tren);
    }


    public void guardarTrenes() {
        this.daoTren.save();
    }

    public void addUser(Usuario usuario) {
        this.daoUsuario.addUser(usuario);
    }

    public void saveUsers() {
        this.daoUsuario.save();
    }

    public void loadUsers() {
        this.daoUsuario.load();
    }


    public void cargaloTodo() {
        // Cargar trenes
        this.daoTren.load();
        // Cargar usuarios
        this.daoUsuario.load();
        // Cargar estaciones
        this.daoEstacion.load();
        // Cargar rutas
        this.daoRuta.load();
        // Cargar circulaciones
        this.daoCirculacion.load();

    }

    public boolean autenticar(String email, String hashedPassword) {
        return this.daoUsuario.autenticar(email, hashedPassword);
    }

    public Usuario encontrarUsuarioPorEmail(String email) {
        return this.daoUsuario.encontrarUsuarioPorEmail(email);
    }

    public List<Estacion> getEstaciones() {
        return this.daoEstacion.estaciones();
    }

    public void actualizarUsuario(String correoAntiguo, Usuario usuario) throws UsuarioNoEncontradoException {
        this.daoUsuario.actualizarUsuario(correoAntiguo, usuario);
    }

    public void guardarUsuarios() {
        this.daoUsuario.save();
    }

    public boolean existeRuta(String origen, String destino) {
        if (origen == null || destino == null) {
            return false;
        }
        return daoRuta.confirmarEnlace(origen, destino);
    }

    public Estacion buscaEstacionPorNombre(String nombreEstacion) {
        return daoEstacion.buscaEstacionPorNombre(nombreEstacion);
    }

    public Ruta buscarRutaPorNombres(String origen, String destino) {
        return daoRuta.buscarRutaPorNombres(origen, destino);
    }

    public Tren localizarTren(String trenId) {
        return daoTren.localizarTren(trenId);
    }

    @TestOnly
    public List<Circulacion> __dbg_obtenerTodasLasCirculaciones() {
        return daoCirculacion.__dbg_circulaciones();
    }

    public List<Circulacion> obtenerCirculacionesRutaEnFecha(Ruta rutaEscogida, LocalDate fechaSalida) {
        return daoCirculacion.obtenerCirculacionesRutaEnFecha(rutaEscogida, fechaSalida);
    }

    public void reservarTren(Usuario usuario, Circulacion circulacion) {
        daoReserva.addReserva(usuario, new Reserva(usuario, circulacion));
    }

    public List<Reserva> getReservasUsuario(Usuario usuario) {
        return daoReserva.localizarReservasUsuario(usuario);
    }
}
