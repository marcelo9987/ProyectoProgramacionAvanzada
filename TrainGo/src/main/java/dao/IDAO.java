package dao;

import java.io.File;

public interface IDAO {
    public default Boolean load(File file) throws UnsupportedOperationException {
        // Se deja así para que sea opcional implementarla
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public default Boolean load(String file) throws UnsupportedOperationException {
        // Se deja así para que sea opcional implementarla
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Boolean load();

    public default Boolean save(File file) throws UnsupportedOperationException {
        // Se deja así para que sea opcional implementarla
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public default Boolean save(String file) throws UnsupportedOperationException {
        // Se deja así para que sea opcional implementarla
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public Boolean save();
}
