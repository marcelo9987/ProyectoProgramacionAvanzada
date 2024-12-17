package dao;

import java.io.File;

/**
 * Interfaz que define los métodos para cargar y guardar datos.
 */
public interface IDAO {
    /**
     * Carga los datos de un archivo específico.
     *
     * @param file archivo a cargar
     * @return true si se cargaron los datos correctamente, false en caso contrario
     * @throws UnsupportedOperationException se deja así para que sea opcional implementarla
     */
    default Boolean load(File file) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Carga los datos de un archivo específico.
     *
     * @param file archivo a cargar
     * @return true si se cargaron los datos correctamente, false en caso contrario
     * @throws UnsupportedOperationException se deja así para que sea opcional implementarla
     */
    default Boolean load(String file) throws UnsupportedOperationException {
        // Se deja así para que sea opcional implementarla
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Carga los datos de un archivo.
     *
     * @return true si se cargaron los datos correctamente, false en caso contrario
     */
    boolean load();

    /**
     * Guarda los datos en un archivo específico.
     *
     * @param file archivo a guardar
     * @return true si se guardaron los datos correctamente, false en caso contrario
     * @throws UnsupportedOperationException se deja así para que sea opcional implementarla
     */
    default Boolean save(File file) throws UnsupportedOperationException {
        // Se deja así para que sea opcional implementarla
        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * Guarda los datos en un archivo específico.
     * @param file archivo a guardar
     * @return true si se guardaron los datos correctamente, false en caso contrario
     * @throws UnsupportedOperationException se deja así para que sea opcional implementarla
     */
    default Boolean save(String file) throws UnsupportedOperationException {
        // Se deja así para que sea opcional implementarla
        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * Guarda los datos en un archivo.
     * @return true si se guardaron los datos correctamente, false en caso contrario
     */
    boolean save();
}
