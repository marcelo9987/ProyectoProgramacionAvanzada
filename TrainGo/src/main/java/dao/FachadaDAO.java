package dao;

import modelo.Tren;
import modelo.Usuario;

public class FachadaDAO {
    private static final String FILE = "trenes.txt";
    private static FachadaDAO instance = null; // Singleton pattern

    private final DAOTren daoTren;
    private final DAOUsuario daoUsuario;

    // Constructor

    private FachadaDAO() {
        daoTren = DAOTren.getInstance();
        daoUsuario = DAOUsuario.getInstance();
    }

    /**
     * Mét.odo que devuelve la instancia de la clase FachadaDAO
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
        this.daoTren.addTren(tren);
    }


    public void loadTren() {
        this.daoTren.load();
    }


    public void updateTren(Tren tren) {
        this.daoTren.updateTren(tren);
    }

    public void saveTren() {
        this.daoTren.save();
    }


    public void deleteTren(Tren tren) {
        this.daoTren.deleteTren(tren);
    }


    public void guardarTrenes() {
        this.daoTren.save();
    }

    public void addUser(Usuario elemental) {
        this.daoUsuario.addUser(elemental);
    }

    public void saveUsers() {
        this.daoUsuario.save();
    }

    public void loadUsers() {
        this.daoUsuario.load();
    }

    public void cargaloTodo() {
        // Cargar trenes
        this.daoTren.load();
        // Cargar usuarios
        this.daoUsuario.load();
        // TODO: EL RESTO DE ENTIDADES

    }

    public boolean autenticar(String email, String hashedPassword) {
        return this.daoUsuario.autenticar(email, hashedPassword);
    }

    public Usuario encontrarUsuarioPorEmail(String email) {
        return this.daoUsuario.encontrarUsuarioPorEmail(email);
    }
}
