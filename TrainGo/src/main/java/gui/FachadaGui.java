package gui;

import aplicacion.FachadaAplicacion;
import gui.formularios.FormularioAutenticacion;
import gui.formularios.FormularioPrincipal;
import org.jetbrains.annotations.TestOnly;

public class FachadaGui {

    private static FachadaGui          instancia = null;
    private final  FachadaAplicacion   fa;
    private        FormularioPrincipal vp;

    private FachadaGui(FachadaAplicacion fa) {
        super();
        this.fa = fa;
    }

    public static FachadaGui getInstance(FachadaAplicacion fa) {
        if (instancia == null) {
            instancia = new FachadaGui(fa);
        }
        return instancia;
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
    @TestOnly
    public void ponerEnMarchaNoAuth()
    {
        vp.dispose();
        vp = new FormularioPrincipal(fa);
    }
}
