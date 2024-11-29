package modelo;

import dao.FachadaDAO;
import gui.FachadaGui;
import modelo.Enums.EnumIdioma;

import java.util.Locale;
import java.util.ResourceBundle;

public class FachadaAplicacion {

    FachadaGui fgui;
    FachadaDAO fdao;

    public ResourceBundle bundle = null;

    public FachadaAplicacion() {
        fgui = new FachadaGui(this); //todo: debería ser un singleton

        bundle = ResourceBundle.getBundle("gui", new Locale("es", "ES"));

        fdao = FachadaDAO.getInstance();
    }

    public static void main(String[] args) {
        FachadaAplicacion fa = new FachadaAplicacion();

        fa.extraerDatosPorDefecto();
        fa.lanzaInterfazGrafica();

    }

    private void extraerDatosPorDefecto() {
        fdao.cargaloTodo();
    }


    private void lanzaInterfazGrafica() {
        fgui.ponerEnMarcha();
    }

    public boolean autenticar(Usuario usuario, String email, String plainPassword) {
        String hashedPassword = util.criptograficos.cifrar(plainPassword);
        plainPassword = null;
        if (fdao.autenticar(email, hashedPassword)) {
            usuario = fdao.encontrarUsuarioPorEmail(email);
            return true;
        }
        return false;
    }

    public void cambiarIdioma(EnumIdioma idioma) {
        switch (idioma) {
            case ESPANHOL:
                bundle = ResourceBundle.getBundle("gui_es_ES", new Locale("es", "ES"));
                break;

            case GALEGO:
                bundle = ResourceBundle.getBundle("gui_gl", new Locale("gl", "ES"));
                break;

            case INGLES:
                bundle = ResourceBundle.getBundle("gui_en_US", new Locale("en", "US"));
                break;
        }
        ResourceBundle.clearCache();
    }

    public ResourceBundle getBundleInstance() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("gui", new Locale("es", "ES"));
        }
        return bundle;
    }

    public void relanzarGUI() {
        fgui.ponerEnMarchaNoAuth();
    }
}
