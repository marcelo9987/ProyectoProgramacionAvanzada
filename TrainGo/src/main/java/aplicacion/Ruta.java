package aplicacion;

public record Ruta(Estacion origen, Estacion destino, int distancia) {
    public String ciudadOrigen() {
        return origen.ciudad();
    }

    public String ciudadDestino() {
        return destino.ciudad();
    }
}

