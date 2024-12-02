package dao;

import modelo.Circulacion;
import modelo.Enums.EnumCirculacion;
import modelo.Ruta;
import modelo.Tren;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("HardCodedStringLiteral")
public class DAOCirculacion extends AbstractDAO {

    @NonNls
    private static final String CIRCULACION = "circulacion";
    @NonNls
    private static final String UUID = "uuid";
    @NonNls
    private static final String TREN = "tren_id";
    @NonNls
    private static final String RUTA = "ruta";
    @NonNls
    private static final String ESTADO = "estado";
    @NonNls
    private static final String HORA_SALIDA = "hora_salida";
    @NonNls
    private static final String HORA_LLEGADA_REAL = "hora_llegada";
    @NonNls
    private static final String PRECIO_POR_ASIENTO = "precio_por_asiento";

    private static DAOCirculacion instance = null; // Singleton pattern
    private final FachadaDAO fa_dao;
    private final Map<Ruta, Circulacion> circulaciones;

    private DAOCirculacion(FachadaDAO fa_dao) {
        super();
        this.fa_dao = fa_dao;
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
        DAOCirculacion daoCirculacion = getInstance(dao);
        daoCirculacion.load();

        for (Circulacion circulacion : daoCirculacion.circulaciones().values()) {
            System.out.println(circulacion);
        }
    }

    private Map<Ruta, Circulacion> circulaciones() {
        return this.circulaciones;
    }



    @Nullable
    @Override
    protected XMLStreamWriter obtenerXMLStreamWriter() {
        this.obtenerLogger();

        XMLStreamWriter writer;

        try {
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            writer = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("circulaciones.xml"));
        } catch (FileNotFoundException | XMLStreamException e) {
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
        } catch (FileNotFoundException | XMLStreamException e) {
            this.logger.error("Error al leer el archivo", e);
            return null;
        }
        return reader;
    }



    @Override
    protected void cargarArchivo(@NotNull XMLEventReader reader) {
        this.obtenerLogger();
        this.logger.trace("Cargando archivo...");


        while (reader.hasNext()) {
                XMLEvent evento;
            try {
                evento = reader.nextEvent();
            } catch (XMLStreamException e) {
                this.logger.error("Error al leer el siguiente evento", e);
                return;
            }

            java.util.UUID uuid;
            String trenId;
            Ruta ruta;
            EnumCirculacion estado;
            LocalDateTime horaSalida;
            LocalDateTime horaLlegadaReal;
            Currency precioPorAsiento;
            int[] fecha = new int[5];

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

                    boolean lecturaIncorrecta = false;
                    while (reader.hasNext()) {
                        try {
                            evento = reader.nextEvent();
                        } catch (XMLStreamException e) {
                            this.logger.error("Error al leer el siguiente evento", e);
                            return;
                        }
                        if (lecturaIncorrecta) {
                            this.logger.error("Lectura incorrecta");
                            return;
                        }


                        if (evento.isStartElement()) {
                            String nombreElementoLeido = evento.asStartElement().getName().getLocalPart();
                            this.logger.debug("Leyendo {}", nombreElementoLeido);
                            switch (nombreElementoLeido) {
                                case UUID -> {
                                    uuid = _procesarUuid(reader, evento);
                                    lecturaIncorrecta = (uuid == null);
                                }
                                case TREN -> {
                                    this.logger.debug("ENTRANDO EN TREN");
                                    trenId = _procesarTren(reader, trenId);
                                    lecturaIncorrecta = (trenId == null);

                                }

                                case RUTA -> {
                                    ruta = _procesarRuta(reader);
                                    lecturaIncorrecta = (ruta == null);
                                }
                                case ESTADO -> {
                                    this.logger.debug("<cargarArchivo> ENTRANDO EN ESTADO");
                                    estado = _procesarEstado(reader, estado);
                                    lecturaIncorrecta = (null == estado);
                                }
                                case HORA_SALIDA -> {

                                    this.logger.debug("ENTRO EN HORA DE SALIDA");

                                    horaSalida = null;
                                    while (reader.hasNext()) {
                                        try {
                                            horaSalida = _gestionarYCrearLaHora(reader, fecha);
                                        } catch (IllegalArgumentException e) {
                                            this.logger.error("Error al gestionar y crear la hora", e);
                                            lecturaIncorrecta = true;
                                        }
                                        if (horaSalida != null) {
                                            break;
                                        }

                                    }

                                }
                                case HORA_LLEGADA_REAL -> {
                                    this.logger.debug("ENTRO EN HORA DE LLEGADA REAL");
                                    horaLlegadaReal = null;
                                    while (reader.hasNext()) {
                                        try {
                                            horaLlegadaReal = _gestionarYCrearLaHora(reader, fecha);
                                        } catch (IllegalArgumentException e) {
                                            this.logger.error("Error al gestionar y crear la hora de llegada real", e);
                                            lecturaIncorrecta = true;
                                        }
                                        if (horaLlegadaReal != null) {
                                            break;
                                        }
                                    }
                                }

                                case PRECIO_POR_ASIENTO -> {
                                    try {
                                        evento = reader.nextEvent();
                                    } catch (XMLStreamException e) {
                                        this.logger.error("Error al leer el precio por asiento", e);
                                        return;
                                    }
                                    if (evento.isCharacters()) {
                                        precioPorAsiento = Currency.getInstance("EUR");
                                        this.logger.debug("Precio por asiento: {}", precioPorAsiento);
                                    }
                                }
                            }
                        }
                        else {
                            if (evento.isEndElement() && evento.asEndElement().getName().getLocalPart().equals(CIRCULACION)) {
                                Circulacion circulacion = fabricarCirculacion(trenId, uuid, ruta, estado, horaSalida, precioPorAsiento);
                                lecturaIncorrecta = (circulacion == null);
                                this.circulaciones.put(ruta, circulacion);
                                this.logger.debug("Circulación añadida: {}", circulacion);

                            }

                        }

                    }
                }
            }


        }

    }

    @Nullable
    private java.util.UUID _procesarUuid(@NotNull XMLEventReader reader, XMLEvent evento) {
        try {
            evento = reader.nextEvent();
        } catch (XMLStreamException e) {
            this.logger.error("Error al leer el UUID: probablemente no exista", e);
        }
        java.util.UUID uuid;
        try {
            uuid = java.util.UUID.fromString(evento.asCharacters().getData());
        } catch (RuntimeException e) {
            this.logger.error("Error al leer el UUID: está mal formado", e);
            return null;
        }
        this.logger.debug("UUID: {}", uuid);
        return uuid;
    }

    @Nullable
    private String _procesarTren(@NotNull XMLEventReader reader, String trenId) {
        XMLEvent evento;
        try {
            evento = reader.nextEvent();
        } catch (XMLStreamException e) {
            this.logger.error("Error al leer el tren: probablemente no exista", e);
            return null;
        }
        if (evento.isCharacters()) {
            trenId = evento.asCharacters().getData();
        }
        this.logger.debug("Añadido Tren: {}", trenId);
        return trenId;
    }

    @Nullable
    private Ruta _procesarRuta(@NotNull XMLEventReader reader) {
        StringBuffer origenSB = new StringBuffer();
        StringBuffer destinoSB = new StringBuffer();
        if (!_procesarRuta(reader, origenSB, destinoSB)) {
            this.logger.error("Error al procesar la ruta");
            return null;
        }
        Ruta ruta;
        try {
            ruta = _comprobarYBuscarRuta(origenSB.toString(), destinoSB.toString());
        } catch (RuntimeException e) {
            this.logger.error("Error al comprobar y buscar la ruta", e);
            return null;
        }
        return ruta;
    }

    @Nullable
    private EnumCirculacion _procesarEstado(@NotNull XMLEventReader reader, EnumCirculacion estado) {
        XMLEvent evento;
        try {
            evento = reader.nextEvent();
        } catch (XMLStreamException e) {
            this.logger.error(" <estado> Error al leer el estado", e);
            return null;
        }
        if (evento.isCharacters()) {
            estado = EnumCirculacion.valueOf(evento.asCharacters().getData());
            this.logger.debug("<estado> Asignado Estado: {}", estado);
        }
        return estado;
    }

    @Nullable
    private LocalDateTime _gestionarYCrearLaHora(@NotNull XMLEventReader reader, int[] fecha) {
        XMLEvent evento;
        try {
            evento = reader.nextEvent();
            this.logger.debug("Leyendo hora de salida");
            this.logger.debug("Evento: {}", evento);
        } catch (XMLStreamException e) {
            this.logger.error("Error al leer el siguiente evento", e);
            return null;
        }

        LocalDateTime horaSalida = null;
        if (evento.isStartElement()) {
            if (!_gestionarDatosEntrantesFecha(reader, evento, fecha)) {
                this.logger.error("Error al gestionar los datos entrantes de la fecha");
                throw new IllegalArgumentException("Error al gestionar los datos entrantes de la fecha");
            }
        }
        if (evento.isEndElement() && evento.asEndElement().getName().getLocalPart().equals(HORA_SALIDA)) {
            horaSalida = _crearFechaHoraYcomprobarValidez(evento, fecha[0], fecha[1], fecha[2], fecha[3], fecha[4]);
        }


        return horaSalida;
    }

    @Nullable
    private Circulacion fabricarCirculacion(String trenId, java.util.UUID uuid, Ruta ruta, EnumCirculacion estado, LocalDateTime horaSalida, Currency precioPorAsiento) {
        this.logger.debug("INICIO PROCESADO DE UNA CIRCULACIÓN");

        // Proceso la data


        Tren tren = fa_dao.localizarTren(trenId);
        if (tren == null) {
            this.logger.error("No se ha encontrado el tren {}: Error al crear la circulación", trenId);

            return null;
        }
        this.logger.trace("Tren encontrado: {}", tren);


        return new Circulacion(uuid, tren, ruta, estado, horaSalida, null, precioPorAsiento);
    }

    private boolean _procesarRuta(@NotNull XMLEventReader reader, StringBuffer origen, StringBuffer destino) {
        this.logger.debug("procesando una ruta");

        while (reader.hasNext()) {
            XMLEvent evento;
            try {
                evento = reader.nextEvent();
            } catch (XMLStreamException e) {
                this.logger.error("Error al leer el siguiente evento", e);
                return false;
            }


            if (evento.isStartElement()) {
                @NonNls String nombreElementoInterno = evento.asStartElement().getName().getLocalPart();
                try {
                    evento = reader.nextEvent();
                } catch (XMLStreamException e) {
                    this.logger.error("Error al leer el siguiente evento", e);
                    return false;
                }

                if (evento.isCharacters()) {
                    String valor = evento.asCharacters().getData();
                    switch (nombreElementoInterno) {
                        case "origen" -> origen.append(valor);
                        case "destino" -> destino.append(valor);
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
        return true;
    }

    @Nullable
    private Ruta _comprobarYBuscarRuta(String origen, String destino) {
        Ruta ruta;
        if (fa_dao.existeRuta(origen, destino)) {
            ruta = fa_dao.buscarRutaPorNombres(origen, destino);
            this.logger.debug("Ruta encontrada: {}", ruta);
        }
        else {
            return null;
        }

        return ruta;
    }

    private boolean _gestionarDatosEntrantesFecha(@NotNull XMLEventReader reader, @NotNull XMLEvent evento, int[] fecha) {
        @NonNls String nombreElementoInterno = evento.asStartElement().getName().getLocalPart();
        try {
            evento = reader.nextEvent();
        } catch (XMLStreamException e) {
            this.logger.error("Error al leer el siguiente evento", e);
            return false;
        }

        if (evento.isCharacters()) {
            String valor = evento.asCharacters().getData();
            switch (nombreElementoInterno) {
                case "año" -> fecha[0] = Integer.parseInt(valor);
                case "mes" -> fecha[1] = Integer.parseInt(valor);
                case "dia" -> fecha[2] = Integer.parseInt(valor);
                case "hora" -> fecha[3] = Integer.parseInt(valor);
                case "minutos" -> fecha[4] = Integer.parseInt(valor);
            }

        }
        return true;
    }

    @Nullable
    private LocalDateTime _crearFechaHoraYcomprobarValidez(@NotNull XMLEvent evento, int anho, int mes, int dia, int hora, int minuto) {
        @NonNls String nombreElementoInterno = evento.asEndElement().getName().getLocalPart();
        this.logger.debug("Procesando hora de salida");
        this.logger.debug("Elemento: {}", nombreElementoInterno);

        if (nombreElementoInterno.equals(HORA_SALIDA)) {
            if (comprobarParametrosDeFechaSonMayoresQueCero(anho, mes, dia, hora, minuto)) {
                this.logger.debug("Hora de salida correcta");
            }
            else {
                this.logger.error("Hora de salida incorrecta");
                return null;
            }
            this.logger.debug("Fin de la hora de salida");
            this.logger.debug("Valores: {} {} {} {} {}", anho, mes, dia, hora, minuto);
            return LocalDateTime.of(anho, mes, dia, hora, minuto);
        }
        // no debería llegar aquí, pero de ser así, es un error
        return null;
    }

    private static boolean comprobarParametrosDeFechaSonMayoresQueCero(int anho, int mes, int dia, int hora, int minuto) {
        System.out.println("Comprobando que los parámetros de la fecha son mayores que cero");
        System.out.println("Año: " + anho + " Mes: " + mes + " Día: " + dia + " Hora: " + hora + " Minuto: " + minuto);
        if (anho > 0) {
            if (mes > 0) {
                if (dia > 0) {
                    if (hora >= 0) {
                        return minuto >= 0;
                    }
                }
            }
        }
        return false;
    }

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
