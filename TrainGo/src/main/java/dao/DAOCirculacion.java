package dao;

import modelo.Circulacion;
import modelo.Enums.EnumCirculacion;
import modelo.Ruta;
import modelo.Tren;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class DAOCirculacion extends AbstractDAO {

    @NonNls
    private static final String CIRCULACION = "circulacion";
    private static final String UUID = "uuid";
    private static final String TREN = "tren_id";
    private static final String RUTA = "ruta";
    private static final String ESTADO = "estado";
    private static final String HORA_SALIDA = "hora_salida";
    private static final String HORA_LLEGADA_REAL = "hora_llegada";
    private static final String PRECIO_POR_ASIENTO = "precio_por_asiento";

    private static DAOCirculacion instance = null; // Singleton pattern
    private final FachadaDAO fadao;
    private final Map<Ruta, Circulacion> circulaciones;

    private DAOCirculacion(FachadaDAO fadao) {
        super();
        this.fadao = fadao;
        this.obtenerLogger();
        this.circulaciones = new HashMap<>();
    }

    public static DAOCirculacion getInstance(FachadaDAO fadao)// Singleton
    {
        if (instance == null) {
            instance = new DAOCirculacion(fadao);
        }
        return instance;
    }

    public static void main(String[] args) {
        FachadaDAO dao = FachadaDAO.getInstance();
        dao.cargaloTodo();
        DAOCirculacion daoCirculacion = DAOCirculacion.getInstance(dao);
        daoCirculacion.load();

        for (Circulacion circulacion : daoCirculacion.circulaciones().values()) {
            System.out.println(circulacion);
        }
    }

    public Map<Ruta, Circulacion> circulaciones() {
        return this.circulaciones;
    }

//        dao.addRutaDesdeComponentes("Madrid", "Barcelona", 600);
//        dao.addRutaDesdeComponentes("Barcelona", "Madrid", 600);
//
//        dao.save();


    @Nullable
    @Override
    protected XMLStreamWriter obtenerXMLStreamWriter() {
        this.obtenerLogger();

        XMLStreamWriter writer;

        try {
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            writer = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("circulaciones.xml"));
        } catch (Exception e) {
            this.logger.error("Error al volcar el archivo", e);
            return null;
        }

        return writer;
    }

    @Nullable
    @Override
    protected XMLEventReader obtenerXmlEventReader() {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader;
        try {
            reader = xmlInputFactory.createXMLEventReader(new FileInputStream("circulaciones.xml"));
        } catch (Exception e) {
            this.logger.error("Error al leer el archivo", e);
            return null;
        }
        return reader;
    }

    @Contract(pure = true)
    private void ponerANull(@NotNull Object... objetos) {
        for (Object objeto : objetos) {
            objeto = null;
        }
    }

    @Override
    protected void cargarArchivo(XMLEventReader reader) {
        this.obtenerLogger();
        this.logger.trace("Cargando archivo...");

        try {
            while (reader.hasNext()) {
                XMLEvent evento;
                try {
                    evento = reader.nextEvent();
                } catch (Exception e) {
                    this.logger.error("Error al leer el siguiente evento", e);
                    return;
                }

                java.util.UUID uuid = null;
                String trenId = null;
                Ruta ruta = null;
                EnumCirculacion estado = null;
                LocalDateTime horaSalida = null;
                LocalDateTime horaLlegadaReal = null;
                Currency precioPorAsiento = null;
                String anho = null, mes = null, dia = null, hora = null, minuto = null;

                if (evento.isStartElement()) {
                    String nombreElemento = evento.asStartElement().getName().getLocalPart();
                    if (nombreElemento.equals(CIRCULACION)) {
                        uuid = null;
                        trenId = null;
                        ruta = null;
                        estado = null;
                        horaSalida = null;
                        horaLlegadaReal = null;
                        precioPorAsiento = null;
                        anho = null;
                        mes = null;
                        dia = null;
                        hora = null;
                        minuto = null;
                        while (reader.hasNext()) {
                            try {
                                evento = reader.nextEvent();
                            } catch (Exception e) {
                                this.logger.error("Error al leer el siguiente evento", e);
                                return;
                            }
                            if (evento.isStartElement()) {
                                String nombreElementoLeido = evento.asStartElement().getName().getLocalPart();
                                this.logger.debug("Leyendo {}", nombreElementoLeido);
                                switch (nombreElementoLeido) {
                                    case UUID -> {
                                        try {
                                            evento = reader.nextEvent();
                                        } catch (Exception e) {
                                            this.logger.error("Error al leer el UUID: probablemente no exista", e);
                                        }
                                        try {
                                            uuid = java.util.UUID.fromString(evento.asCharacters().getData());
                                        } catch (Exception e) {
                                            this.logger.error("Error al leer el UUID: está mal formado", e);
                                            return;
                                        }
                                        this.logger.debug("UUID: {}", uuid);
                                    }

                                    case TREN -> {
                                        this.logger.debug("ENTRANDO EN TREN");
                                        try {
                                            evento = reader.nextEvent();
                                        } catch (Exception e) {
                                            this.logger.error("Error al leer el tren", e);
                                        }
                                        if (evento.isCharacters()) {
                                            trenId = evento.asCharacters().getData();
                                        }
                                        this.logger.debug("Añadido Tren: {}", trenId);
                                    }

                                    case RUTA -> {

                                        this.logger.debug("ENTRANDO EN RUTA");

                                        String origen = null;
                                        String destino = null;

//                                        this.logger.debug("Leyendo ruta. Cabecera: {}", evento);

                                        while (reader.hasNext()) {
                                            try {
                                                evento = reader.nextEvent();
                                            } catch (Exception e) {
                                                this.logger.error("Error al leer el siguiente evento", e);
                                                return;
                                            }


                                            if (evento.isStartElement()) {
                                                @NonNls String nombreElementoInterno = evento.asStartElement().getName().getLocalPart();
                                                try {
                                                    evento = reader.nextEvent();
                                                } catch (Exception e) {
                                                    this.logger.error("Error al leer el siguiente evento", e);
                                                    return;
                                                }

                                                if (evento.isCharacters()) {
                                                    String valor = evento.asCharacters().getData();
                                                    this.logger.debug("<<ruta>> Valor: {}", valor);
                                                    switch (nombreElementoInterno) {
                                                        case "origen" -> origen = valor;
                                                        case "destino" -> destino = valor;
                                                    }
                                                }
                                            }
                                            else if (evento.isEndElement()) {
                                                @NonNls String nombreElementoInterno = evento.asEndElement().getName().getLocalPart();
                                                if (nombreElementoInterno.equals("ruta")) {
                                                    this.logger.debug("<<ruta>> Fin de la ruta");
                                                    break;
                                                }
                                            }

                                        }

                                        if (origen != null && destino != null) {
                                            if (fadao.existeRuta(origen, destino)) {
                                                try {
                                                    ruta = fadao.buscarRutaPorNombres(origen, destino);
                                                    this.logger.debug("Ruta encontrada: {}", ruta);
                                                } catch (Exception e) {
                                                    this.logger.error("La ruta no existe o ha pasado algo inesperado", e);
                                                }
                                            }
                                            else {
                                                this.logger.error("No se ha encontrado la ruta de origen {} a destino {}", origen, destino);
                                                ponerANull(uuid, trenId, ruta, estado, horaSalida, horaLlegadaReal, precioPorAsiento);
                                            }
                                        }
                                    }
                                    case ESTADO -> {
                                        this.logger.debug("ENTRANDO EN ESTADO");
                                        try {
                                            evento = reader.nextEvent();
                                        } catch (Exception e) {
                                            this.logger.error("Error al leer el estado", e);
                                            ponerANull(uuid, trenId, ruta, estado, horaSalida, horaLlegadaReal, precioPorAsiento);
                                            return;
                                        }
                                        if (evento.isCharacters()) {
                                            estado = EnumCirculacion.valueOf(evento.asCharacters().getData());
                                            this.logger.debug("Asignado Estado: {}", estado);
                                        }
                                    }
                                    case HORA_SALIDA -> {
                                        this.logger.debug("ENTRO EN HORA DE SALIDA");

                                        while (reader.hasNext()) {
                                            try {
                                                evento = reader.nextEvent();
                                                this.logger.debug("Leyendo hora de salida");
                                                this.logger.debug("Evento: {}", evento);
                                            } catch (Exception e) {
                                                this.logger.error("Error al leer el siguiente evento", e);
                                                return;
                                            }
                                            if (evento.isStartElement()) {
                                                @NonNls String nombreElementoInterno = evento.asStartElement().getName().getLocalPart();
                                                try {
                                                    evento = reader.nextEvent();
                                                } catch (Exception e) {
                                                    this.logger.error("Error al leer el siguiente evento", e);
                                                    return;
                                                }

                                                if (evento.isCharacters()) {
                                                    String valor = evento.asCharacters().getData();
                                                    switch (nombreElementoInterno) {
                                                        case "año" -> anho = valor == null ? "2" : valor;
                                                        case "mes" -> mes = valor == null ? "55" : valor;
                                                        case "dia" -> dia = valor == null ? "55" : valor;
                                                        case "hora" -> hora = valor == null ? "55" : valor;
                                                        case "minutos" -> minuto = valor == null ? "55" : valor;
                                                    }
                                                }
                                            }
                                            else if (evento.isEndElement()) {
                                                @NonNls String nombreElementoInterno = evento.asEndElement().getName().getLocalPart();
                                                if (nombreElementoInterno.equals(HORA_SALIDA)) {
                                                    this.logger.debug("Fin de la hora de salida");
                                                    this.logger.debug("Valores: {} {} {} {} {}", anho, mes, dia, hora, minuto);
                                                    break;
                                                }

                                            }

                                        }

                                    }
                                    case HORA_LLEGADA_REAL -> {
                                        // de momento nada hazme: modularizar?
                                        this.logger.debug("Hora de llegada real detectada, pasando de largo");
                                    }

                                    case PRECIO_POR_ASIENTO -> {
                                        try {
                                            evento = reader.nextEvent();
                                        } catch (Exception e) {
                                            this.logger.error("Error al leer el precio por asiento", e);
                                            ponerANull(uuid, trenId, ruta, estado, horaSalida, horaLlegadaReal
                                                    , precioPorAsiento);
                                            return;
                                        }
                                        if (evento.isCharacters()) {
                                            precioPorAsiento = Currency.getInstance("EUR");
                                            this.logger.debug("Precio por asiento: {}", precioPorAsiento);
                                        }
                                    }
                                }
                            }
                            else if (evento.isEndElement() && evento.asEndElement().getName().getLocalPart().equals(CIRCULACION)) {
                                this.logger.debug("FINAL DE UN ELEMENTO A CONTINUACION");
                                this.logger.debug("Fin de un elemento: {}", evento.asEndElement().getName().getLocalPart());
                                if (evento.asEndElement().getName().getLocalPart().equals(CIRCULACION)) {

                                    this.logger.debug("INICIO PROCESADO DE UNA CIRCULACIÓN");

                                    // Proceso la data
                                    horaSalida = LocalDateTime.of(Integer.parseInt(anho), Integer.parseInt(mes), Integer.parseInt(dia), Integer.parseInt(hora), Integer.parseInt(minuto));


                                    Tren tren = fadao.localizarTren(trenId);
                                    if (tren == null) {
                                        this.logger.error("No se ha encontrado el tren {}", trenId);
                                        ponerANull(uuid, trenId, ruta, estado, horaSalida, horaLlegadaReal
                                                , precioPorAsiento);
                                        continue;
                                    }
                                    else {
                                        this.logger.trace("Tren encontrado: {}", tren);
                                    }

//                        LocalDateTime timeHoraLlegadaReal = null;
//                        if (horaLlegadaReal != null) {
//                            timeHoraLlegadaReal = LocalDateTime.parse(horaLlegadaReal);
//                        }


                                    Circulacion circulacion = new Circulacion(uuid, tren, ruta, estado, horaSalida, null, precioPorAsiento);
                                    this.circulaciones.put(ruta, circulacion);

                                }
                                else {
                                    this.logger.error("no he leido lo esperado, leido: {}", evento.asEndElement().getName().getLocalPart());
                                }
                            }

                        }
                    }
                }


            }


        } catch (Exception e) {
            this.logger.error("Error al leer el archivo", e);
        }
    }

//    private void addRutaDesdeComponentes(String origen, String destino, int distancia) {
//        Estacion estacionOrigen = null;
//        Estacion estacionDestino = null;
//        for (Estacion estacion : FachadaDAO.getInstance().getEstaciones()) {
//            if (estacion.ciudad().equals(origen)) {
//                estacionOrigen = estacion;
//            }
//            if (estacion.ciudad().equals(destino)) {
//                estacionDestino = estacion;
//            }
//        }
//        if (estacionOrigen == null) {
//            this.logger.error("No se ha encontrado la estación de origen {}", origen);
//            if (estacionDestino == null) {
//                this.logger.error("No se ha encontrado la estación de destino {}", destino);
//            }
//            return;
//        }
//        if (estacionDestino == null) {
//            this.logger.error("No se ha encontrado la estación de destino {}", destino);
//            return;
//        }
//        this.ci  rutas().putIfAbsent(estacionOrigen, new HashMap<Estacion, Ruta>());
//        this
//                .rutas
//                .get(estacionOrigen)
//                .put(estacionDestino, new Ruta(estacionOrigen, estacionDestino, distancia));
//    }


    @Override
    protected void guardarArchivo(XMLStreamWriter writer) {
//        this.logger.trace("Guardando contenido en el archivo...");
//        try {
//            writer.writeStartDocument();
//            writer.writeStartElement("rutas");
//        } catch (Exception e) {
//            this.logger.error("Error al escribir cabecera. Es posible que el haya dejado de existir (o el acceso a mutex haya sido revocado)", e);
//        }
//
//        for (Map<Estacion, Ruta> ruta : rutas.values()) {
//            for (Ruta r : ruta.values()) {
//                try {
//                    writer.writeStartElement("ruta");
//
//                    writer.writeStartElement("origen");
//                    writer.writeCharacters(r.ciudadOrigen());
//                    writer.writeEndElement();
//
//                    writer.writeStartElement("destino");
//                    writer.writeCharacters(r.ciudadDestino());
//                    writer.writeEndElement();
//
//                    writer.writeStartElement("distancia");
//                    writer.writeCharacters(String.valueOf(r.distancia()));
//                    writer.writeEndElement();
//
//                    writer.writeEndElement();
//                } catch (Exception e) {
//                    this.logger.error("¡¡CRITICO!! --> Error al escribir la ruta. LA INFORMACIÓN PUEDE ESTAR CORRUPTA", e);
//                }
//            }
//        }
//
//        try {
//            writer.writeEndElement();
//            writer.writeEndDocument();
//        } catch (Exception e) {
//            this.logger.error("¡¡CRITICO!! --> Error al escribir el final del documento. LA INFORMACIÓN PUEDE ESTAR CORRUPTA", e);
//        }
//
    }


}
