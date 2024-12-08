package dao;

import aplicacion.Circulacion;
import aplicacion.Ruta;
import aplicacion.Tren;
import aplicacion.anotaciones.NoNegativo;
import aplicacion.enums.EnumCirculacion;
import aplicacion.excepciones.LecturaSiguienteEventoException;
import aplicacion.excepciones.ProcesadoSiguienteEventoException;
import dao.constantes.ConstantesGeneral;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.*;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class DAOCirculacion extends AbstractDAO {

    @NonNls
    private static final String CIRCULACION       = "circulacion";
    @NonNls
    private static final String UUID              = "uuid";
    @NonNls
    private static final String TREN              = "tren_id";
    @NonNls
    private static final String RUTA              = "ruta";
    @NonNls
    private static final String ESTADO            = "estado";
    @NonNls
    private static final String HORA_SALIDA       = "hora_salida";
    @NonNls
    private static final String HORA_LLEGADA_REAL = "hora_llegada";
    @NonNls
    private static final String PRECIO_POR_ASIENTO = "precio_por_asiento";

    private static DAOCirculacion               instance = null; // Singleton pattern
    private final  FachadaDAO                   fa_dao;
    private final  Map<Ruta, List<Circulacion>> circulaciones;

    private DAOCirculacion(FachadaDAO fa_dao) {
        super();
        this.fa_dao = fa_dao;
        this.obtenerLogger();
        this.circulaciones = new HashMap<>();
    }

    @TestOnly
    public static void main(String[] args) {
        FachadaDAO dao = FachadaDAO.getInstance();
        dao.cargaloTodo();
        DAOCirculacion daoCirculacion = getInstance(dao);
        daoCirculacion.load();

        for (Ruta r : daoCirculacion.circulaciones().keySet()) {
            for (Circulacion c : daoCirculacion.circulaciones().get(r)) {
                System.out.println(c);
            }
        }

        daoCirculacion.save();
    }

    public static DAOCirculacion getInstance(FachadaDAO fadao)// Singleton
    {
        if (instance == null) {
            instance = new DAOCirculacion(fadao);
        }
        return instance;
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

    @Override
    protected void guardarArchivo(@NotNull XMLStreamWriter writer) {
//        _abrirCabeceraCirculaciones(writer);
        abrirCabeceraArchivoXML(writer, ConstantesGeneral.FICHERO_CIRCULACION);
        List<List<Circulacion>> circulacionesApiladas = new ArrayList<>(this.circulaciones().values());

        List<Circulacion> circulacionesSinApilar = new ArrayList<>();
        for (Iterator<List<Circulacion>> it = circulacionesApiladas.iterator(); it.hasNext(); ) {
            List<Circulacion> participanteNoApilada = it.next();
            circulacionesSinApilar.addAll(participanteNoApilada);
        }

        for (Circulacion circulacion : circulacionesSinApilar) {
            try {
                writer.writeStartElement(CIRCULACION);

                escribirElemento(writer, UUID, circulacion.id().toString());

                escribirElemento(writer, TREN, String.valueOf(circulacion.trenId()));

                _escribirRuta(writer, circulacion);

                //  ----------------- ESTADO -----------------

                escribirElemento(writer, ESTADO, circulacion.estado().toString());

                //  ----------------- HORA SALIDA -----------------


                _escribirFechaHora(writer, circulacion.horaSalida(), HORA_SALIDA);


                if (circulacion.horaLlegadaReal() != null) {
                    writer.writeStartElement(HORA_LLEGADA_REAL);
                    _escribirFechaHora(writer, circulacion.horaLlegadaReal(), HORA_LLEGADA_REAL);

                    writer.writeEndElement();
                }
                escribirElemento(writer, PRECIO_POR_ASIENTO, circulacion.precioPorAsiento().toString());
                writer.writeEndElement();

            } catch (XMLStreamException e) {
                this.logger.error("Error al escribir la circulación", e);
                throw new IllegalArgumentException("Error al escribir la circulación");
            }
        }
        try {
            writer.writeEndDocument();
        } catch (XMLStreamException e) {
            this.logger.error("Error al cerrar el archivo", e);
            throw new IllegalArgumentException("Error al cerrar el archivo");
        }

        this.logger.info("Archivo guardado de forma satisfactoria");
    }

    private Map<Ruta, List<Circulacion>> circulaciones() {
        return this.circulaciones;
    }

    /**
     * Escribe un elemento en un archivo XML.
     *
     * @param writer      el XMLStreamWriter que se usará para escribir el archivo
     * @param circulacion el elemento a escribir
     * @throws XMLStreamException si hay un error al escribir el archivo
     */
    private void _escribirRuta(@NotNull XMLStreamWriter writer, @NotNull Circulacion circulacion) throws XMLStreamException {
        writer.writeStartElement(RUTA);
        escribirElemento(writer, "origen", String.valueOf(circulacion.nombreCiudadOrigen()));
        escribirElemento(writer, "destino", String.valueOf(circulacion.nombreCiudadDestino()));
        writer.writeEndElement();
    }

    /**
     * Escribe una fecha y hora en un archivo XML.
     *
     * @param writer      el XMLStreamWriter que se usará para escribir el archivo
     * @param circulacion la fecha y hora a escribir
     * @param tipoHora    el tipo de hora a escribir, puede ser "hora_salida" u "hora_llegada_real"
     * @throws XMLStreamException si hay un error al escribir el archivo
     */
    private void _escribirFechaHora(@NotNull XMLStreamWriter writer, @NotNull LocalDateTime circulacion, @NotNull @MagicConstant(stringValues = {HORA_SALIDA, HORA_LLEGADA_REAL}) String tipoHora) throws XMLStreamException {

        if (!tipoHora.equals(HORA_SALIDA) && !tipoHora.equals(HORA_LLEGADA_REAL)) {
            throw new IllegalArgumentException("Tipo de hora incorrecto");
        }


        writer.writeStartElement(tipoHora);

        escribirElemento(writer, "año", String.valueOf(circulacion.getYear()));

        escribirElemento(writer, "mes", String.valueOf(circulacion.getMonthValue()));

        escribirElemento(writer, "dia", String.valueOf(circulacion.getDayOfMonth()));

        escribirElemento(writer, "hora", String.valueOf(circulacion.getHour()));

        escribirElemento(writer, "minutos", String.valueOf(circulacion.getMinute()));

        writer.writeEndElement();
    }

    @Override
    protected void cargarArchivo(@NotNull XMLEventReader reader) {
        this.logger.trace("Cargando archivo...");


        while (reader.hasNext()) {
            XMLEvent evento = getNextXmlEvent(reader);

            if (evento.isStartElement() && evento.asStartElement().getName().getLocalPart().equals(CIRCULACION)) {
                try {
                    _procesarCirculacion(reader);
                } catch (Exception e) {
                    throw new ProcesadoSiguienteEventoException( e);
                }
            }

        }

        for (Ruta r : circulaciones.keySet()) {
            for (Circulacion c : circulaciones.get(r)) {
                this.logger.trace("Circulación cargada: {}", c);
            }
        }

    }

    @Nullable
    @Override
    protected XMLEventReader obtenerXmlEventReader() {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader  reader;
        try {
            reader = xmlInputFactory.createXMLEventReader(new FileInputStream("circulaciones.xml"));
        } catch (FileNotFoundException | XMLStreamException e) {
            this.logger.error("Error al leer el archivo", e);
            return null;
        }
        return reader;
    }

    /**
     * Procesa la cabecera de un archivo XML de circulaciones.
     *
     * @param writer el XMLStreamWriter que se usará para escribir el archivo
     */
    @Deprecated(forRemoval = true)
    @Contract(pure = true)
    private void _abrirCabeceraCirculaciones(@NotNull XMLStreamWriter writer) {
        try {
            writer.writeStartDocument();
            writer.writeStartElement("circulaciones");
        } catch (XMLStreamException e) {
            this.logger.error("Error al escribir la cabecera", e);
            throw new IllegalArgumentException("Error al escribir la cabecera");
        }
    }

    private void _procesarCirculacion(@NotNull XMLEventReader reader) {
        UUID            uuid             = null;
        String          trenId           = null;
        Ruta            ruta             = null;
        EnumCirculacion estado           = null;
        LocalDateTime   horaSalida       = null, horaLlegadaReal = null;
        BigDecimal      precioPorAsiento = null;
        int[]           fecha            = new int[5];

        while (reader.hasNext()) {
            XMLEvent evento = getNextXmlEvent(reader);


            if (evento.isStartElement()) {
                switch (evento.asStartElement().getName().getLocalPart()) {
                    case UUID -> uuid = _procesarUuid(reader);
                    case TREN -> trenId = _procesarTren(reader, trenId);
                    case RUTA -> ruta = _procesarEstacionesRuta(reader);
                    case ESTADO -> estado = _procesarEstado(reader, estado);
                    case HORA_SALIDA -> horaSalida = _gestionarYCrearLaHora(reader, fecha);
                    case HORA_LLEGADA_REAL -> horaLlegadaReal = _gestionarYCrearLaHora(reader, fecha);
                    case PRECIO_POR_ASIENTO -> precioPorAsiento = _procesarPrecioPorAsiento(reader);

                    default -> throw new IllegalStateException("Unexpected value: " + evento.asStartElement().getName().getLocalPart());
                }
            }
            else if (evento.isEndElement() && evento.asEndElement().getName().getLocalPart().equals(CIRCULACION)) {
                _crearEinsertarCirculacion(trenId, uuid, ruta, estado, horaSalida, horaLlegadaReal, precioPorAsiento);
                break;
            }

        }
    }

    @NotNull
    private UUID _procesarUuid(@NotNull XMLEventReader reader) {

        XMLEvent evento = getNextXmlEvent(reader);

        UUID uuid;
        try {
            uuid = java.util.UUID.fromString(evento.asCharacters().getData());
        } catch (RuntimeException e) {
            this.logger.error("Error al leer el UUID: está mal formado", e);
            throw new IllegalArgumentException("Error al leer el UUID: está mal formado");
        }
        this.logger.debug("UUID: {}", uuid);
        return uuid;
    }

    @NotNull
    private String _procesarTren(@NotNull XMLEventReader reader, String trenId) {
        XMLEvent evento = this.getNextXmlEvent(reader);

        if (evento.isCharacters()) {
            trenId = evento.asCharacters().getData();
        }

        this.logger.debug("Añadido Tren: {}", trenId);
        return trenId;
    }

    @NotNull
    private Ruta _procesarEstacionesRuta(@NotNull XMLEventReader reader) {
        StringBuffer origenSB  = new StringBuffer();
        StringBuffer destinoSB = new StringBuffer();

        _procesarEstacionesRuta(reader, origenSB, destinoSB);

        if (origenSB.isEmpty() || destinoSB.isEmpty()) {
            this.logger.error("Error al procesar la ruta, origen o destino vacíos");
            throw new IllegalArgumentException("Error al procesar la ruta, origen o destino vacíos");
        }

        Ruta ruta;
        try {
            ruta = _comprobarYBuscarRuta(origenSB.toString(), destinoSB.toString());
        } catch (RuntimeException e) {
            this.logger.error("Error al comprobar y buscar la ruta", e);
            this.logger.error("Error al comprobar y buscar la ruta: No existe la ruta");
            throw new IllegalArgumentException("Error al comprobar y buscar la ruta: No existe la ruta");
        }
        if (ruta == null) {
            this.logger.error("Error al comprobar y buscar la ruta: Ruta nula");
            throw new IllegalArgumentException("Error al comprobar y buscar la ruta: Ruta nula");
        }
        return ruta;
    }

    @NotNull
    private EnumCirculacion _procesarEstado(@NotNull XMLEventReader reader, EnumCirculacion estado) {

        XMLEvent evento = this.getNextXmlEvent(reader);

        if (evento.isCharacters()) {
            estado = EnumCirculacion.valueOf(evento.asCharacters().getData());
            this.logger.debug("<estado> Asignado Estado: {}", estado);
        }
        return estado;
    }

    @NotNull
    private LocalDateTime _gestionarYCrearLaHora(@NotNull XMLEventReader reader, int[] fecha) {

        XMLEvent evento;// = this.getNextXmlEvent(reader);

        LocalDateTime horaSalida;

        do {
            if (!reader.hasNext()) {
                throw new LecturaSiguienteEventoException();
            }
            evento = this.getNextXmlEvent(reader);

            if (evento.isStartElement()) {
                _gestionarDatosEntrantesFecha(reader, evento, fecha);
            }

        } while (_comprobarDentroDeHora(evento));


        horaSalida = _crearFechaHoraYcomprobarValidez(evento, fecha[0], fecha[1], fecha[2], fecha[3], fecha[4]);


        return horaSalida;
    }

    @NotNull
    private BigDecimal _procesarPrecioPorAsiento(@NotNull XMLEventReader reader) {
        BigDecimal precioPorAsiento;
        XMLEvent   evento = getNextXmlEvent(reader);
        if (!evento.isCharacters()) {
            this.logger.error("Error al leer el precio por asiento: no es un número");
            throw new IllegalArgumentException("Error al leer el precio por asiento: no es un número");
        }

        precioPorAsiento = new BigDecimal(evento.asCharacters().getData());
        this.logger.debug("Precio por asiento: {}", precioPorAsiento);

        return precioPorAsiento;
    }

    private void _crearEinsertarCirculacion(String trenId, java.util.UUID uuid, Ruta ruta, EnumCirculacion estado, LocalDateTime horaSalida, LocalDateTime horaLlegadaReal, BigDecimal precioPorAsiento) {
//        Circulacion circulacion = Circulacion.fabricarCirculacion(trenId, uuid, ruta, estado, horaSalida, horaLlegadaReal, precioPorAsiento);
        Tren tren = fa_dao.localizarTren(trenId);
        if (tren == null) {
            this.logger.error("Error al crear la circulación: tren nulo");
            throw new IllegalArgumentException("Error al crear la circulación: tren nulo");
        }
        Circulacion circulacion = new Circulacion(uuid, tren, ruta, estado, horaSalida, horaLlegadaReal, precioPorAsiento);

        this.circulaciones.putIfAbsent(ruta, new ArrayList<>());
        this.circulaciones.get(ruta).add(circulacion);
        this.logger.debug("Circulación añadida: {}", circulacion);
    }

    private void _procesarEstacionesRuta(@NotNull XMLEventReader reader, StringBuffer origen, StringBuffer destino) {
        this.logger.debug("procesando una ruta");

        while (reader.hasNext()) {

            XMLEvent evento = this.getNextXmlEvent(reader);

            if (evento.isStartElement()) {
                @NonNls String nombreElementoInterno = evento.asStartElement().getName().getLocalPart();

                evento = this.getNextXmlEvent(reader);

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
    }

    @Nullable
    private Ruta _comprobarYBuscarRuta(String origen, String destino) {
        Ruta ruta;
        if (!fa_dao.existeRuta(origen, destino)) {
            this.logger.error("Ruta no encontrada");
            throw new IllegalArgumentException("Ruta no encontrada");
        }

        ruta = fa_dao.buscarRutaPorNombres(origen, destino);
        this.logger.debug("Ruta encontrada: {}", ruta);

        return ruta;
    }

    private void _gestionarDatosEntrantesFecha(@NotNull XMLEventReader reader, @NotNull XMLEvent evento, int[] fecha) {
        @NonNls String nombreElementoInterno = evento.asStartElement().getName().getLocalPart();

        evento = this.getNextXmlEvent(reader);

        this.logger.debug("Leyendo {}", evento.asCharacters().getData());

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
    }

    private boolean _comprobarDentroDeHora(@NotNull XMLEvent evento) {
        if (evento.isEndElement()) {
            return !evento.asEndElement().getName().getLocalPart().equals(HORA_SALIDA)
                    && !evento.asEndElement().getName().getLocalPart().equals(HORA_LLEGADA_REAL);
        }
        return true;
    }

    @NotNull
    private LocalDateTime _crearFechaHoraYcomprobarValidez(@NotNull XMLEvent evento, @NoNegativo int anho, @NoNegativo int mes, @NoNegativo int dia, @NoNegativo int hora,@NoNegativo int minuto) {
        @NonNls String nombreElementoInterno = evento.asEndElement().getName().getLocalPart();
        this.logger.debug("Procesando{}", nombreElementoInterno);
        this.logger.debug("Elemento: {}", nombreElementoInterno);

        if (nombreElementoInterno.equals(HORA_SALIDA) || nombreElementoInterno.equals(HORA_LLEGADA_REAL)) {
            if (comprobarParametrosDeFechaSonMayoresQueCero(anho, mes, dia, hora, minuto)) {
                this.logger.debug("Hora correcta");
            }
            else {
                this.logger.error("Hora incorrecta");
                throw new IllegalArgumentException("Formato de fecha incorrecto");
            }
            this.logger.debug("Fin de la hora");
            this.logger.debug("Valores: {} {} {} {} {}", anho, mes, dia, hora, minuto);
            return LocalDateTime.of(anho, mes, dia, hora, minuto);
        }
        // no debería llegar aquí, pero de ser así, es un error
        this.logger.error("Error al crear la fecha y hora");
        throw new IllegalArgumentException("Error al crear la fecha y hora");
    }

    private static boolean comprobarParametrosDeFechaSonMayoresQueCero(@NoNegativo int anho, @NoNegativo int mes, @NoNegativo int dia, @NoNegativo int hora,@NoNegativo int minuto) {
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

    /**
     * Busca las circulaciones de una ruta en una fecha concreta.
     *
     * @param rutaEscogida la ruta de la que se quieren obtener las circulaciones
     * @param fechaSalida  la fecha en la que se quieren obtener las circulaciones
     * @return una lista con las circulaciones de la ruta en la fecha dada
     */
    List<Circulacion> obtenerCirculacionesRutaEnFecha(Ruta rutaEscogida, LocalDate fechaSalida) {
        List<Circulacion> circulacionesEnRuta = this.circulaciones().values().stream().flatMap(Collection::stream).toList().stream().filter(circulacion -> circulacion.ruta().equals(rutaEscogida)).toList();
        if (circulacionesEnRuta.isEmpty()) {
            this.logger.error("No se han encontrado circulaciones para la ruta {}", rutaEscogida);
            return List.of();
        }
        List<Circulacion> circulacionesEnFecha = new ArrayList<>();
        for (Circulacion circulacion : circulacionesEnRuta) {
            if (circulacion.horaSalida().toLocalDate().equals(fechaSalida)) {
                circulacionesEnFecha.add(circulacion);
            }
        }
        return circulacionesEnFecha;
    }

    /**
     * Localiza una circulación por su UUID.
     *
     * @param idCirculacion el UUID de la circulación a localizar
     * @return la circulación localizada
     */
    @Contract(pure = true)
    Circulacion localizarCirculacion(@org.hibernate.validator.constraints.UUID UUID idCirculacion) {
        for (List<Circulacion> circulaciones : this.circulaciones().values()) {
            for (Circulacion circulacion : circulaciones) {
                if (circulacion.id().equals(idCirculacion)) {
                    return circulacion;
                }
            }
        }
        this.logger.error("Circulación no encontrada");
        throw new IllegalArgumentException("Circulación no encontrada");
    }
}



