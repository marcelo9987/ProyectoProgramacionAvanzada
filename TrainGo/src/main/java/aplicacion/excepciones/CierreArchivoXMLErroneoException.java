package aplicacion.excepciones;

import javax.xml.stream.XMLStreamException;

/**
 * Excepción lanzada cuando se produce un error al cerrar el archivo XML
 */
public final class CierreArchivoXMLErroneoException extends XMLStreamException {
    /**
     * Constructor sin parámetros
     */
    public CierreArchivoXMLErroneoException() {
        super("Error al cerrar el archivo");
    }

    /**
     * Constructor con un mensaje ampliado por excepción original
     *
     * @param e excepción original
     */
    public CierreArchivoXMLErroneoException(Exception e) {
        super("Error al cerrar el archivo", e);
    }
}
