package com.marcesoft.traingo.dao;

import com.marcesoft.traingo.aplicacion.excepciones.CargaArchivoFallidaException;
import com.marcesoft.traingo.aplicacion.excepciones.CierreArchivoXMLErroneoException;
import com.marcesoft.traingo.aplicacion.excepciones.LecturaSiguienteEventoException;
import com.marcesoft.traingo.dao.constantes.ConstantesGeneral;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;

/**
 * Clase AbstractDAO
 * Clase abstracta que implementa la interfaz IDAO
 * Se encarga de la carga y guardado de archivos XML
 * Implementa el patrón Template Method y es un esqueleto para las clases DAO
 */
abstract class AbstractDAO implements IDAO {

    Logger logger;

    /**
     * Carga el archivo XML en la base de datos. Usa el patrón Template Method.
     *
     * @return true si la carga fue exitosa, false en caso contrario
     */
    @Override
    public boolean load() {
        XMLEventReader reader = null;
        obtenerLogger(); // no hace nada si todo está bien, y si no, inicializa logger
        logger.info("Cargando archivo...");

        try {
            reader = obtenerXmlEventReader();
            cargarArchivo(reader);
        } catch (CargaArchivoFallidaException e) {
            logger.error("Error al cargar el archivo", e);
            return false;
        } finally {
            try {
                cerrarArchivo(reader);
            } catch (CierreArchivoXMLErroneoException e) {
                logger.error("Error al cerrar el archivo", e);
            }
        }

        logger.info("Archivo cargado de forma satisfactoria");
        return true;
    }

    /**
     * Guarda el archivo XML en la base de datos. Usa el patrón Template Method.
     *
     * @return true si la carga fue exitosa, false en caso contrario
     */
    @Override
    public boolean save() {
        XMLStreamWriter writer;
        obtenerLogger();
        logger.info("Guardando archivo...");


        try {
            writer = obtenerXMLStreamWriter();
            if (writer == null) {
                logger.error("Error al obtener el XMLStreamWriter");
                return false;
            }
        } catch (RuntimeException e) {
            logger.error("Error al obtener el XMLStreamWriter", e);
            return false;
        }

        guardarArchivo(writer);


        try {
            cerrarArchivo(writer);
        } catch (CierreArchivoXMLErroneoException e) {
            logger.error("Error al cerrar el archivo. Es posible que no se haya guardado correctamente", e);
            return false;
        }
        logger.info("Archivo guardado de forma satisfactoria y cerrado correctamente");

        return true;
    }

    /**
     * Obtiene un XMLStreamWriter para escribir en un archivo XML.
     *
     * @return un XMLStreamWriter para el contenedor específico
     */
    abstract protected XMLStreamWriter obtenerXMLStreamWriter();

    /**
     * Guarda el archivo XML en la base de datos. Es parte del patrón Template Method.
     *
     * @param writer el XMLStreamWriter que se usará para escribir el archivo
     */
    abstract protected void guardarArchivo(XMLStreamWriter writer);

    /**
     * Cierra el archivo XML.
     *
     * @param writer el XMLStreamWriter que se usará para cerrar el archivo
     * @throws CierreArchivoXMLErroneoException si hay un error al cerrar el archivo
     */
    private void cerrarArchivo(@NotNull XMLStreamWriter writer) throws CierreArchivoXMLErroneoException {
        try {
            writer.close();
        } catch (XMLStreamException e) {
            logger.error("Error al cerrar el archivo", e);
            throw new CierreArchivoXMLErroneoException();
        }
    }

    /**
     * Obtiene el logger de la clase.
     */
    void obtenerLogger() {
        if (logger != null) {
            return;
        }
        logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Obtiene un XMLEventReader para leer un archivo XML.
     *
     * @return un XMLEventReader para el contenedor específico
     */
    abstract protected XMLEventReader obtenerXmlEventReader();

    /**
     * Carga el archivo XML en la base de datos. Es parte del patrón Template Method.
     *
     * @param reader el XMLEventReader que se usará para leer el archivo
     */
    abstract protected void cargarArchivo(XMLEventReader reader);

    /**
     * Cierra el archivo XML.
     *
     * @param reader el XMLEventReader que se usará para cerrar el archivo
     * @throws CierreArchivoXMLErroneoException si hay un error al cerrar el archivo
     */
    private void cerrarArchivo(XMLEventReader reader) throws CierreArchivoXMLErroneoException {
        try {
            if (reader == null) {
                throw new IllegalArgumentException("El reader no puede ser null");
            }
            reader.close();
        } catch (XMLStreamException e) {
            throw new CierreArchivoXMLErroneoException(e);
        }
    }

    @NotNull
    XMLEvent getNextXmlEvent(@NotNull XMLEventReader reader) {
        XMLEvent evento;
        try {
            evento = reader.nextEvent();
        } catch (XMLStreamException e) {
            this.logger.error("Error al procesar el siguiente evento", e);
            throw new LecturaSiguienteEventoException();
        }

        // Jamás será null, ya que si hay un error, se lanza una excepción
        return evento;
    }

    void escribirElemento(@NotNull XMLStreamWriter writer, String name, String value) throws XMLStreamException {
        writer.writeStartElement(name);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    void escribirAperturaCabeceraArchivoXML(@NotNull XMLStreamWriter writer, @MagicConstant(stringValues = {ConstantesGeneral.FICHERO_CIRCULACION, ConstantesGeneral.FICHERO_ESTACION, ConstantesGeneral.FICHERO_RESERVA, ConstantesGeneral.FICHERO_RESERVA, ConstantesGeneral.FICHERO_TREN, ConstantesGeneral.FICHERO_USUARIO, ConstantesGeneral.FICHERO_RUTA}) String tipo) {

        if (!ConstantesGeneral.tipoValido(tipo)) {
            this.logger.error("Tipo inválido");
            throw new IllegalArgumentException("Tipo inválido");
        }

        try {
            writer.writeStartDocument();
            writer.writeStartElement(tipo);
        } catch (XMLStreamException e) {
            this.logger.error("Error al escribir la cabecera", e);
            throw new IllegalArgumentException("Error al escribir la cabecera");
        }
    }


    }
