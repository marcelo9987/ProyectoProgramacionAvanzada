import dao.FachadaDAO;
import modelo.Tren;
import modelo.Usuario;

import java.util.Date;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        FachadaDAO fachada = FachadaDAO.getInstance();

        // Trenes
        fachada.addTren(new Tren(UUID.randomUUID(), 1));

        fachada.guardarTrenes();

        fachada.loadTren();

        fachada.guardarTrenes();

        // Usuarios

        Usuario elemental = new Usuario(2569685, "a", "a@a.a", "a", 25696665, "TEst 24", new Date(21 / 11 / 1965), true);

        fachada.addUser(elemental);

        fachada.saveUsers();
        fachada.loadUsers();
        fachada.saveUsers();

    }
}
