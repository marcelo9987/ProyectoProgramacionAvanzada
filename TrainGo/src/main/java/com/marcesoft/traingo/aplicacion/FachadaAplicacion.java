package com.marcesoft.traingo.aplicacion;

import com.marcesoft.traingo.aplicacion.anotaciones.NoNegativo;
import com.marcesoft.traingo.aplicacion.enums.EnumIdioma;
import com.marcesoft.traingo.aplicacion.excepciones.UsuarioNoEncontradoException;
import com.marcesoft.traingo.dao.FachadaDAO;
import com.marcesoft.traingo.gui.FachadaGui;
import jakarta.validation.constraints.Email;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.TestOnly;
import org.slf4j.helpers.CheckReturnValue;
import com.marcesoft.traingo.util.Criptograficos;
import com.marcesoft.traingo.util.Internacionalizacion;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Clase que permite abstraer la lógica de la aplicación.
 */
public final class FachadaAplicacion{


    // Atributos

    private final Internacionalizacion itz;
    private final FachadaGui     fgui;
    private final FachadaDAO     fdao;
    private       ResourceBundle bundle;
    private       Usuario        usuario;

    /**
     * Fachada de la aplicación.
     */
    public FachadaAplicacion() {
        super();

        itz = Internacionalizacion.getInstance();

        fdao = FachadaDAO.getInstance();

        bundle = itz.getBundle();

        fgui = FachadaGui.getInstance(this);

    }

    /**
     * Método principal de la Fachada.
     *
     * @param args No se usa,
     */
    @TestOnly
    public static void main(String[] args) {
        FachadaAplicacion fa = new FachadaAplicacion();

        fa.extraerDatosPorDefecto();
        fa.lanzaInterfazGrafica();
     }

    /**
     * Método que permite ejecutar la aplicación.
     * Pone en marcha la interfaz gráfica y extrae los datos por defecto.
     */
     public void ejecutar()
     {
        extraerDatosPorDefecto();
        lanzaInterfazGrafica();
     }

    /**
     * Método que extrae los datos almacenados en los ficheros por defecto
     */
    private void extraerDatosPorDefecto() {
        fdao.cargarTodosLosDatosDeFicheros();
    }

    /**
     * Método que lanza la interfaz gráfica. Llamada en cascada.
     */
    private void lanzaInterfazGrafica() {
        fgui.ponerEnMarcha();
    }

    /**
     * Permite comprobar si una tupla de usuario y contraseña es válida y se tiene constancia de ella en la «base de datos».
     *
     * @param email         Email del usuario.
     * @param plainPassword Contraseña en texto plano.
     * @return true si la tupla es válida, false en caso contrario.
     */
    @Contract("null, _ -> false; _, null -> false")
    public boolean autenticar(String email, String plainPassword) {
        String hashedPassword = Criptograficos.cifrar(plainPassword);
        // Eliminamos la contraseña en texto plano
        if (fdao.autenticar(email, hashedPassword)) {
            this.usuario = fdao.encontrarUsuarioPorEmail(email);
            return true;
        }
        return false;
    }

    /**
     * Devuelve el bundle de internacionalización.
     *
     * @return ResourceBundle con las traducciones en el idioma actual.
     */
    public ResourceBundle getBundleInstance() {
        if (bundle == null) {
            bundle = itz.getBundle();
        }
        return itz.getBundle();
    }

    /**
     * Reinicia la interfaz gráfica.
     * Está pensado para cuando se cambia el idioma. Puede ser útil en otros casos.
     */
    public void relanzarGUI() {
        fgui.ponerEnMarchaNoAuth();
    }

    /**
     * Método que permite cambiar el idioma de la aplicación.
     *
     * @param idioma Idioma al que se quiere cambiar.
     *               Opciones:
     *               ESPAÑOL,
     *               GALEGO,
     *               INGLES.
     * @see Internacionalizacion#cambiarIdioma(EnumIdioma)
     */
    public void cambiarIdioma(EnumIdioma idioma) {
        itz.cambiarIdioma(idioma);

    }

    /**
     * Método que permite obtener las estaciones de la base de datos.
     *
     * @return Devuelve una lista (Vectorizada) con las estaciones de la «base de datos».
     * @see FachadaDAO#getEstaciones()
     */
    @Contract(pure = true)
    @CheckReturnValue // ¡¡Puede estar vacío!! (Algo ha fallado)
    public List<Estacion> getEstaciones() {
        return fdao.getEstaciones();
    }

    /**
     * Permite obtener el dni del usuario autenticado.
     *
     * @return DNI del usuario autenticado.
     */
    public int obtenerDniUsuario() {
        return this.usuario.DNI();
    }

    /**
     * Permite obtener el nombre del usuario autenticado.
     * @return Nombre del usuario autenticado.
     */
    public String getUsrNombre() {
        return this.usuario.nombre();
    }

    /**
     * Permite obtener el email del usuario autenticado.
     * @return Correo electrónico del usuario autenticado.
     */
    public String getUsrCorreo() {
        return this.usuario.correo();
    }

    /**
     * Permite obtener el teléfono del usuario autenticado.
     * @return Teléfono del usuario autenticado.
     */
    public int getUsrTelefono() {
        return this.usuario.telefono();
    }

    /**
     * Permite obtener la dirección del usuario autenticado.
     * @return Dirección del usuario autenticado.
     */
    public String getUsrDireccion() {
        return this.usuario.direccion();
    }

    /**
     * Método que permite actualizar los datos de un usuario.
     *
     * @param correoAntiguo  Correo antiguo del usuario.
     * @param nuevoCorreo    Nuevo correo del usuario.
     * @param nuevaDireccion Nueva dirección del usuario.
     * @param nuevoTelefono  Nuevo teléfono del usuario.
     * @throws UsuarioNoEncontradoException Cuando no se encuentra el usuario.
     */
    public void actualizarUsuario(String correoAntiguo, @Email String nuevoCorreo, String nuevaDireccion, @NoNegativo int nuevoTelefono) throws UsuarioNoEncontradoException {
        fdao.actualizarUsuario(correoAntiguo, this.usuario);
        this.usuario.actualizarDatos(nuevoCorreo, nuevaDireccion, nuevoTelefono);
        fdao.guardarUsuarios();
    }

    /**
     * Método que permite buscar una ruta por sus nombres de origen y destino.
     * @param origen  Nombre de la estación de origen.
     * @param destino Nombre de la estación de destino.
     * @return Ruta que une las dos estaciones.
     */
    public Ruta buscarRutaPorNombres(String origen, String destino) {
        return fdao.buscarRutaPorNombres(origen, destino);
    }

    /**
     * Método que permite obtener las circulaciones de una ruta concreta en una fecha concreta.
     * @param rutaEscogida Ruta de la que se quieren obtener las circulaciones.
     * @param fechaSalida Fecha en la que se quieren obtener las circulaciones.
     * @return Lista de circulaciones de la ruta en la fecha dada.
     */
    public List<Circulacion> obtenerCirculacionesRuta(Ruta rutaEscogida, LocalDate fechaSalida) {
        return fdao.obtenerCirculacionesRutaEnFecha(rutaEscogida, fechaSalida);
    }

    /**
     * Método que permite reservar un tren para el usuario autenticado.
     * @param circulacion Circulación que se quiere reservar.
     */
    public void reservarTren(Circulacion circulacion) {
        fdao.reservarTren(this.usuario, circulacion);
    }

    /**
     * Getter de usuario.
     * @return Usuario autenticado.
     */
    public Usuario usuario() {
        return usuario;
    }

    /**
     * Método que permite obtener las reservas de un usuario.
     * @param usuario Usuario del que se quieren obtener las reservas.
     * @return Lista de reservas del usuario.
     */
    public List<Reserva> getReservasUsuario(Usuario usuario) {
        return fdao.getReservasUsuario(usuario);
    }

}
