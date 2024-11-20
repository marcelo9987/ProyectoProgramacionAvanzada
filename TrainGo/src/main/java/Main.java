import dao.FachadaDAO;
import modelo.Tren;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        FachadaDAO fachada = FachadaDAO.getInstance();
        fachada.addTren(new Tren(UUID.randomUUID(), 1));
    }
}
