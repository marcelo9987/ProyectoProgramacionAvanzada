package aplicacion.excepciones;

import javax.xml.stream.XMLStreamException;

public final class CierreArchivoXMLErroneoException extends XMLStreamException {
    public CierreArchivoXMLErroneoException() {
        super("Error al cerrar el archivo");
    }

    public CierreArchivoXMLErroneoException(Exception e) {
        super("Error al cerrar el archivo", e);
    }
}
