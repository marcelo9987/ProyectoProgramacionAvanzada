package com.marcesoft.traingo.dao;


import com.marcesoft.traingo.aplicacion.*;
import com.marcesoft.traingo.aplicacion.excepciones.UsuarioNoEncontradoException;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * FachadaDAO
 * Clase que permite abstraer la lógica de acceso a datos.
 */
public class FachadaDAO {
    private static volatile FachadaDAO instance = null; // Singleton pattern

    private final DAOTren     daoTren;
    private final DAOUsuario  daoUsuario;
    private final DAOEstacion daoEstacion;
    private final DAORuta     daoRuta;
    private final DAOCirculacion daoCirculacion;
    private final DAOReserva  daoReserva;

    /**
     * Constructor de la clase FachadaDAO. Como se trata de un Singleton, es privado.
     */
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
        if(instance != null) {
            return instance;
        }
        synchronized(FachadaDAO.class) {
            if(instance == null) {
                instance = new FachadaDAO();
            }
        }
        return instance;
    }


    /**
     * Método que carga todos los datos de la aplicación de sus respectivos archivos XML
     */
    public void cargarTodosLosDatosDeFicheros() {
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

    /**
     * Método que intenta autenticar a un usuario
     *
     * @param email          Email del usuario
     * @param hashedPassword Contraseña cifrada del usuario
     * @return true en caso de éxito, false en cualquier otro caso
     */
    public boolean autenticar(String email, String hashedPassword) {
        return this.daoUsuario.autenticar(email, hashedPassword);
    }

    /**
     * Método que devuelve un usuario a partir de su email
     * @param email Email del usuario
     * @return Usuario con el email dado si existe, null en caso contrario
     */
    @Nullable
    public Usuario encontrarUsuarioPorEmail(String email) {
        return this.daoUsuario.encontrarUsuarioPorEmail(email);
    }

    /**
     * Método que devuelve todas las estaciones de la aplicación
     * @return Estaciones de la aplicación
     */
    public List<Estacion> getEstaciones() {
        return this.daoEstacion.estaciones();
    }

    /**
     * Actualiza los datos de un usuario
     * @param correoAntiguo Correo antiguo del usuario
     * @param usuario Datos actualizados para el usuario
     * @throws UsuarioNoEncontradoException Si no se encuentra el usuario
     */
    public void actualizarUsuario(String correoAntiguo, Usuario usuario) throws UsuarioNoEncontradoException {
        this.daoUsuario.actualizarUsuario(correoAntiguo, usuario);
    }

    /**
     * Guarda los usuarios en el archivo XML
     */
    public void guardarUsuarios() {
        this.daoUsuario.save();
    }

    /**
     *  Comprueba que haya una ruta entre dos estaciones
     * @param origen estación de origen
     * @param destino estación de destino
     * @return true si existe una ruta entre las dos estaciones, false en cualquier otro caso
     */
    boolean existeRuta(String origen, String destino) {
        if (origen == null || destino == null) {
            return false;
        }
        return daoRuta.confirmarEnlace(origen, destino);
    }

    /** Método que permite buscar una estación por su nombre
     * @param nombreEstacion Nombre de la estación a buscar
     * @return Estación con el nombre dado
     */
    Estacion buscaEstacionPorNombre(String nombreEstacion) {
        return daoEstacion.buscaEstacionPorNombre(nombreEstacion);
    }

    /**
     * Método que permite buscar una ruta por sus nombres de origen y destino.
     * @param origen Nombre de la estación de origen.
     * @param destino Nombre de la estación de destino.
     * @return Ruta que une las dos estaciones.
     */
    public Ruta buscarRutaPorNombres(String origen, String destino) {
        return daoRuta.buscarRutaPorNombres(origen, destino);
    }

    /**
     * Método que permite obtener todas las rutas de la aplicación
     * @param trenId Identificador del tren
     * @return Lista de rutas del tren
     */
    Tren localizarTren(String trenId) {
        return daoTren.localizarTren(trenId);
    }

    /**
     * Método que permite obtener las circulaciones de una ruta en una fecha dada
     * @param rutaEscogida Ruta de la que se quieren obtener las circulaciones
     * @param fechaSalida Fecha en la que se quieren obtener las circulaciones
     * @return Lista de circulaciones de la ruta en la fecha dada
     */
    public List<Circulacion> obtenerCirculacionesRutaEnFecha(Ruta rutaEscogida, LocalDate fechaSalida) {
        return daoCirculacion.obtenerCirculacionesRutaEnFecha(rutaEscogida, fechaSalida);
    }

    /**
     * Método que permite reservar un tren para un usuario
     * @param usuario Usuario que quiere reservar el tren
     * @param circulacion Circulación que se quiere reservar
     */
    public void reservarTren(Usuario usuario, Circulacion circulacion) {
        daoReserva.addReservaDesdeCirculacion(usuario, circulacion);
    }

    /**
     * Método que permite obtener las reservas de un usuario
     * @param usuario Usuario del que se quieren obtener las reservas
     * @return Lista de reservas del usuario
     */
    public List<Reserva> getReservasUsuario(Usuario usuario) {
        return daoReserva.localizarReservasUsuario(usuario);
    }

    /**
     * Permite encontrar a un usuario por su DNI
     * @param usuarioDNI DNI del usuario
     * @return Usuario con el DNI dado
     * @throws UsuarioNoEncontradoException Si no se encuentra el usuario
     */
    Usuario encontrarUsuarioPorDNI(String usuarioDNI) throws UsuarioNoEncontradoException {
        return daoUsuario.encontrarUsuarioPorDNI(usuarioDNI);
    }

    /** Localiza una circulación por su identificador
     * @param id_circulacion Identificador de la circulación
     * @return Circulación con el identificador dado
     */
    Circulacion localizarCirculacion(UUID id_circulacion) {
        return daoCirculacion.localizarCirculacion(id_circulacion);
    }
}
