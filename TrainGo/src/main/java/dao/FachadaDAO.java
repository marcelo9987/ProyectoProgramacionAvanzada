package dao;

import aplicacion.*;
import aplicacion.excepciones.UsuarioNoEncontradoException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

        daoReserva = DAOReserva.getInstance(this);
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

        // Cargar reservas
        this.daoReserva.load();

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

    boolean existeRuta(String origen, String destino) {
        if (origen == null || destino == null) {
            return false;
        }
        return daoRuta.confirmarEnlace(origen, destino);
    }

    Estacion buscaEstacionPorNombre(String nombreEstacion) {
        return daoEstacion.buscaEstacionPorNombre(nombreEstacion);
    }

    public Ruta buscarRutaPorNombres(String origen, String destino) {
        return daoRuta.buscarRutaPorNombres(origen, destino);
    }

    Tren localizarTren(String trenId) {
        return daoTren.localizarTren(trenId);
    }


    public List<Circulacion> obtenerCirculacionesRutaEnFecha(Ruta rutaEscogida, LocalDate fechaSalida) {
        return daoCirculacion.obtenerCirculacionesRutaEnFecha(rutaEscogida, fechaSalida);
    }

    public void reservarTren(Usuario usuario, Circulacion circulacion) {
        daoReserva.addReservaDesdeCirculacion(usuario, circulacion);
    }

    public List<Reserva> getReservasUsuario(Usuario usuario) {
        return daoReserva.localizarReservasUsuario(usuario);
    }

    Usuario encontrarUsuarioPorDNI(String usuarioDNI) throws UsuarioNoEncontradoException {
        return daoUsuario.encontrarUsuarioPorDNI(usuarioDNI);
    }

    Circulacion localizarCirculacion(UUID id_circulacion) {
        return daoCirculacion.localizarCirculacion(id_circulacion);
    }
}
