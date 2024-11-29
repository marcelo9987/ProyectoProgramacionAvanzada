package gui;

import gui.formularios.FormularioAutenticacion;
import gui.formularios.FormularioPrincipal;
import modelo.FachadaAplicacion;

public class FachadaGui {

    FormularioPrincipal vp;
    FachadaAplicacion fa;

    public FachadaGui(FachadaAplicacion fa) {
        this.fa = fa;
        this.vp = new FormularioPrincipal(fa);
    }

    public void ponerEnMarcha() {
        FormularioAutenticacion va = new FormularioAutenticacion(this.fa, this.vp, true);
        va.setVisible(true);

    }

    public void ponerEnMarchaNoAuth() {
        vp.dispose();
        vp = new FormularioPrincipal(fa);
    }
}
