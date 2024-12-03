import aplicacion.Tren;
import aplicacion.Usuario;
import dao.FachadaDAO;

import java.time.LocalDate;
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

        Usuario elemental = new Usuario(2569685, "a", "a@a.a", "a", 25696665, "TEst 24", LocalDate.parse("2004-11-20"), true);

        fachada.addUser(elemental);

        fachada.saveUsers();
        fachada.loadUsers();
        fachada.saveUsers();

    }
}
