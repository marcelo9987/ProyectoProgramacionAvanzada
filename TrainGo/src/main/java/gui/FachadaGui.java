package gui;

import aplicacion.FachadaAplicacion;
import gui.formularios.FormularioAutenticacion;
import gui.formularios.FormularioPrincipal;

public class FachadaGui {

    FormularioPrincipal vp;
    FachadaAplicacion fa;

    public FachadaGui(FachadaAplicacion fa) {
        super();
        this.fa = fa;
    }

    /**
     * Pone en marcha la interfaz gráfica
     */
    public void ponerEnMarcha() {
        vp = new FormularioPrincipal(fa);
        vp.setVisible(true);

        FormularioAutenticacion va = new FormularioAutenticacion(this.fa, this.vp, true);
        va.setVisible(true);

    }

    /**
     * Pone en marcha la interfaz gráfica sin autenticación.
     *
     * @implNote Pensado para:
     * - Pruebas.
     * - Cambios de idioma.
     */
    public void ponerEnMarchaNoAuth() // mírame: ¿Posible vulnerabilidad?
    {
        vp.dispose();
        vp = new FormularioPrincipal(fa);
    }
}
