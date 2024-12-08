package dao;

import java.io.File;

public interface IDAO {
    default Boolean load(File file) throws UnsupportedOperationException {
        // Se deja así para que sea opcional implementarla
        throw new UnsupportedOperationException("Not supported yet.");
    }

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

    default Boolean save(File file) throws UnsupportedOperationException {
        // Se deja así para que sea opcional implementarla
        throw new UnsupportedOperationException("Not supported yet.");
    }


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
