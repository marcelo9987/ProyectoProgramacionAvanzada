package dao;

import aplicacion.Estacion;
import aplicacion.Ruta;
import aplicacion.excepciones.SituacionDeRutasInesperadaException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DAORuta extends AbstractDAO {
    private static DAORuta instance = null; // Singleton pattern
    private final FachadaDAO fadao;
    private final Map<Estacion, Map<Estacion, Ruta>> rutas;

    private DAORuta(FachadaDAO fadao) {
        super();
        this.fadao = fadao;
        this.obtenerLogger();
        this.rutas = new HashMap<>();
    }

    public static void main(String[] args) {
        DAORuta dao = getInstance(FachadaDAO.getInstance());
        FachadaDAO.getInstance().cargaloTodo();
        XMLEventReader xmlEvtRdr_lector = dao.obtenerXmlEventReader();
        dao.cargarArchivo(xmlEvtRdr_lector);

        for (Map<Estacion, Ruta> ruta : dao.getRutas().values()) {
            for (Ruta r : ruta.values()) {
                System.out.println(r);
            }
        }

//        dao.addRutaDesdeComponentes("Madrid", "Barcelona", 600);
//        dao.addRutaDesdeComponentes("Barcelona", "Madrid", 600);
//
//        dao.save();
    }

    public static DAORuta getInstance(FachadaDAO fadao)// Singleton
    {
        if (instance == null) {
            instance = new DAORuta(fadao);
        }
        return instance;
    }

    public Map<Estacion, Map<Estacion, Ruta>> getRutas() {
        return this.rutas;
    }

    private void addRutaDesdeComponentes(String origen, String destino, int distancia) {
        Estacion estacionOrigen  = null;
        Estacion estacionDestino = null;
        for (Estacion estacion : FachadaDAO.getInstance().getEstaciones()) {
            if (estacion.ciudad().equals(origen)) {
                estacionOrigen = estacion;
            }
            if (estacion.ciudad().equals(destino)) {
                estacionDestino = estacion;
            }
        }
        if (estacionOrigen == null) {
            this.logger.error("No se ha encontrado la estación de origen {}", origen);
            if (estacionDestino == null) {
                this.logger.error("No se ha encontrado la estación de destino {}", destino);
            }
            return;
        }
        if (estacionDestino == null) {
            this.logger.error("No se ha encontrado la estación de destino {}", destino);
            return;
        }
        this.rutas.putIfAbsent(estacionOrigen, new HashMap<>());
        this
                .rutas
                .get(estacionOrigen)
                .put(estacionDestino, new Ruta(estacionOrigen, estacionDestino, distancia));
    }

    @Nullable
    @Override
    protected XMLStreamWriter obtenerXMLStreamWriter() {
        this.obtenerLogger();

        XMLStreamWriter writer;

        try {
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            writer = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("rutas.xml"));
        } catch (Exception e) {
            this.logger.error("Error al volcar el archivo", e);
            return null;
        }

        return writer;
    }

    @Override
    protected void guardarArchivo(@NotNull XMLStreamWriter writer) {
        this.logger.trace("Guardando contenido en el archivo...");
        try {
            writer.writeStartDocument();
            writer.writeStartElement("rutas");
        } catch (XMLStreamException e) {
            this.logger.error("Error al escribir cabecera. Es posible que el haya dejado de existir (o el acceso a mutex haya sido revocado)", e);
        }

        for (Map<Estacion, Ruta> ruta : rutas.values()) {
            for (Ruta r : ruta.values()) {
                escribirRuta(writer, r);
            }
        }

        try {
            writer.writeEndElement();
            writer.writeEndDocument();
        } catch (Exception e) {
            this.logger.error("¡¡CRITICO!! --> Error al escribir el final del documento. LA INFORMACIÓN PUEDE ESTAR CORRUPTA", e);
        }

    }

    @Nullable
    @Override
    protected XMLEventReader obtenerXmlEventReader() {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader;
        try {
            reader = xmlInputFactory.createXMLEventReader(new FileInputStream("rutas.xml"));
        } catch (Exception e) {
            this.logger.error("Error al leer el archivo", e);
            return null;
        }
        return reader;
    }

    @Override
    protected void cargarArchivo(XMLEventReader reader) {
        this.obtenerLogger();
        this.logger.trace("Cargando archivo...");

        try {
            while (reader.hasNext()) {
                XMLEvent evento = this.getNextXmlEvent(reader);

                if (evento.isStartElement()) {
                    String nombreElemento = evento.asStartElement().getName().getLocalPart();
                    if (nombreElemento.equals("ruta")) {
                        String origen = null;
                        String destino = null;
                        int distancia = 0;

                        while (reader.hasNext()) {
                            evento = this.getNextXmlEvent(reader);

                            if (evento.isStartElement()) {
                                @NonNls String nombreElementoInterno = evento.asStartElement().getName().getLocalPart();
                                evento = this.getNextXmlEvent(reader);

                                if (evento.isCharacters()) {
                                    String valor = evento.asCharacters().getData();
                                    switch (nombreElementoInterno) {
                                        case "origen" -> origen = valor;
                                        case "destino" -> destino = valor;
                                        case "distancia" -> distancia = Integer.parseInt(valor);
                                    }
                                }
                            }
                            else if (evento.isEndElement()) {
                                @NonNls String nombreElementoInterno = evento.asEndElement().getName().getLocalPart();
                                if (nombreElementoInterno.equals("ruta")) {
                                    break;
                                }
                            }
                        }

                        if (origen != null && destino != null) {
                            this.logger.trace("Añadiendo ruta de {} a {} con distancia de {}", origen, destino, Integer.valueOf(distancia));
                            this.addRutaDesdeComponentes(origen, destino, distancia);

                        }
                    }
                }
            }
        } catch (Exception e) {
            this.logger.error("Error al leer el archivo", e);
        }
    }

    private void escribirRuta(@NotNull XMLStreamWriter writer, @NotNull Ruta ruta) {
        try {
            writer.writeStartElement("ruta");

            escribirElemento(writer, "origen", ruta.ciudadOrigen());

            escribirElemento(writer, "destino", ruta.ciudadDestino());

            escribirElemento(writer, "distancia", String.valueOf(ruta.distancia()));

            writer.writeEndElement();
        } catch (XMLStreamException e) {
            this.logger.error("¡¡CRITICO!! --> Error al escribir la ruta. LA INFORMACIÓN PUEDE ESTAR CORRUPTA", e);
        }
    }

    boolean confirmarEnlace(@NotNull String origen, @NonNls @NotNull String destino) {
        if (origen.isEmpty() || destino.isEmpty()) {
            return false;
        }


        Estacion estacionOrigen = this.rutas.keySet().stream().filter(e -> e.ciudad().equals(origen)).findFirst().get();
        Estacion estacionDestino = this.fadao.buscaEstacionPorNombre(destino);
        return this.rutas.get(estacionOrigen).containsKey(estacionDestino);

        //        Estacion estacionDestino = this.rutas.get(estacionOrigen).keySet().stream().filter(e -> e.ciudad().equals(destino)).findFirst().get();

    }

    public Ruta buscarRutaPorNombres(@NonNls @NotNull String origen, @NotNull @NonNls String destino) {
        Estacion estacionOrigen = this.rutas.keySet().stream().filter(e -> e.ciudad().equals(origen)).findFirst().get();
        Estacion estacionDestino = this.fadao.buscaEstacionPorNombre(destino);

        if (!this.rutas.get(estacionOrigen).containsKey(estacionDestino)) {
            throw new SituacionDeRutasInesperadaException("No se ha encontrado la ruta entre " + origen + " y " + destino);
        }

        return this.rutas.get(estacionOrigen).get(estacionDestino);
    }
}
