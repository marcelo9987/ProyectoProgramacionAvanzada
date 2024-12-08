package aplicacion;

import aplicacion.anotaciones.NoNegativo;
import aplicacion.enums.EnumIdioma;
import aplicacion.excepciones.UsuarioNoEncontradoException;
import dao.FachadaDAO;
import gui.FachadaGui;
import jakarta.validation.constraints.Email;
import org.jetbrains.annotations.Contract;
import org.slf4j.helpers.CheckReturnValue;
import util.Criptograficos;
import util.Internacionalizacion;

import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public final class FachadaAplicacion {

    // Atributos

    private final Internacionalizacion itz;
    private final FachadaGui     fgui;
    private final FachadaDAO     fdao;
    private       ResourceBundle bundle;
    private       Usuario        usuario;

    public FachadaAplicacion() {
        super();

        itz = Internacionalizacion.getInstance();

        fdao = FachadaDAO.getInstance();

        bundle = itz.getBundle();

        fgui = FachadaGui.getInstance(this);

    }

    public static void main(String[] args) {
        FachadaAplicacion fa = new FachadaAplicacion();

        fa.extraerDatosPorDefecto();
        fa.lanzaInterfazGrafica();
        // pruebo miUsuario
//        fa.lanzaInterfazGrafica();

    }

    /**
     * Método que extrae los datos almacenados en los ficheros por defecto
     *
     * @implNote No se tiene intención de permitir en un futuro próximo la carga de rutas personalizadas.
     */
    private void extraerDatosPorDefecto() {
        fdao.cargaloTodo();
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

    public int getUsrDni() {
        return this.usuario.DNI();
    }

    public String getUsrNombre() {
        return this.usuario.nombre();
    }

    public String getUsrCorreo() {
        return this.usuario.correo();
    }

    public int getUsrTelefono() {
        return this.usuario.telefono();
    }

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

    public Ruta buscarRutaPorNombres(String origen, String destino) {
        return fdao.buscarRutaPorNombres(origen, destino);
    }

    public List<Circulacion> obtenerCirculacionesRuta(Ruta rutaEscogida, LocalDate fechaSalida) {
        return fdao.obtenerCirculacionesRutaEnFecha(rutaEscogida, fechaSalida);
    }

    public void reservarTren(Circulacion circulacion) {
        fdao.reservarTren(this.usuario, circulacion);
    }

    public Usuario usuario() {
        return usuario;
    }

    public List<Reserva> getReservasUsuario(Usuario usuario) {
        return fdao.getReservasUsuario(usuario);
    }
}
