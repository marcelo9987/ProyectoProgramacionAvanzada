package modelo;

import dao.FachadaDAO;
import gui.FachadaGui;
import modelo.Enums.EnumIdioma;
import util.Criptograficos;
import util.Internacionalizacion;

import java.util.ResourceBundle;

public class FachadaAplicacion {

    FachadaGui fgui;
    FachadaDAO fdao;
    public ResourceBundle bundle;
    Internacionalizacion itz;

    public FachadaAplicacion() {

        itz = Internacionalizacion.getInstance();

        fgui = new FachadaGui(this); //todo: ¿debería ser un singleton?

        bundle = itz.getBundle();

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
        String hashedPassword = Criptograficos.cifrar(plainPassword);
        plainPassword = null; // Eliminamos la contraseña en texto plano
        if (fdao.autenticar(email, hashedPassword)) {
            usuario = fdao.encontrarUsuarioPorEmail(email);
            return true;
        }
        return false;
    }

    public ResourceBundle getBundleInstance() {
        if (bundle == null) {
            bundle = itz.getBundle();
        }
        return itz.getBundle();
    }

    public void relanzarGUI() {
        fgui.ponerEnMarchaNoAuth();
    }

    public void cambiarIdioma(EnumIdioma idioma) {
        itz.cambiarIdioma(idioma);
    }
}
