package com.marcesoft.traingo.gui;

import com.marcesoft.traingo.aplicacion.FachadaAplicacion;
import com.marcesoft.traingo.gui.formularios.FormularioAutenticacion;
import com.marcesoft.traingo.gui.formularios.FormularioPrincipal;
import org.jetbrains.annotations.TestOnly;

/**
 * Fachada de la interfaz gráfica
 */
public class FachadaGui {

    private static FachadaGui          instancia = null;
    private final  FachadaAplicacion   fa;
    private        FormularioPrincipal vp;

    private FachadaGui(FachadaAplicacion fa) {
        super();
        this.fa = fa;
    }

    /**
     * Obtiene la instancia de la fachada de la interfaz gráfica
     *
     * @param fa Fachada de la aplicación
     * @return Instancia de la fachada de la interfaz gráfica
     */
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
     * Pensado para:
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
