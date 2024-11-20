package dao;

import modelo.Tren;

public class FachadaDAO {
    private static final String FILE = "trenes.txt";
    private static FachadaDAO instance = null; // Singleton pattern ()
    private DAOTren daoTren = null;

    // Constructor

    private FachadaDAO() {
        daoTren = DAOTren.getInstance();
    }

    /**
     * Método que devuelve la instancia de la clase FachadaDAO
     *
     * @return instance Instancia de la clase FachadaDAO. Si no existe, la crea
     */
    public static FachadaDAO getInstance() {
        if (instance == null) {
            instance = new FachadaDAO();
        }
        return instance;
    }

    // BUSINESS LOGIC

    // CRUD Tren (Crear, Leer, Actualizar, Borrar)

    public void addTren(Tren tren) {
        DAOTren.getInstance().addTren(tren);
    }


    public void loadTren() {
        DAOTren.getInstance().load();
    }


    public void updateTren(Tren tren) {
        DAOTren.getInstance().updateTren(tren);
    }

    public void saveTren() {
        DAOTren.getInstance().save();
    }


    public void deleteTren(Tren tren) {
        DAOTren.getInstance().deleteTren(tren);
    }


}
