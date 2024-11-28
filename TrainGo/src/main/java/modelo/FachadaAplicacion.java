package modelo;

import dao.FachadaDAO;
import gui.FachadaGui;

public class FachadaAplicacion {

    FachadaGui fgui;
    FachadaDAO fdao;

    public FachadaAplicacion() {
        fgui = new FachadaGui(this); //todo: debería ser un singleton
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
}
