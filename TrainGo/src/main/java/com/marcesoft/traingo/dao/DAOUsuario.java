package com.marcesoft.traingo.dao;

import com.marcesoft.traingo.aplicacion.Usuario;
import com.marcesoft.traingo.aplicacion.excepciones.LecturaSiguienteEventoException;
import com.marcesoft.traingo.aplicacion.excepciones.SituacionDeRutasInesperadaException;
import com.marcesoft.traingo.aplicacion.excepciones.UsuarioNoEncontradoException;
import com.marcesoft.traingo.aplicacion.excepciones.noHayUsuariosRegistradosException;
import com.marcesoft.traingo.dao.constantes.ConstantesGeneral;
import com.marcesoft.traingo.dao.constantes.TagsXMLUsuario;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Clase que gestiona el IO y la persistencia de los usuarios
 */
public class DAOUsuario extends AbstractDAO {

    private static DAOUsuario instance = null;
    private final  List<Usuario> usuarios;

    private DAOUsuario() {
        super();
        this.obtenerLogger();
        this.usuarios = new ArrayList<>();
    }

    /**
     * Método main para pruebas
     *
     * @param args no se usa
     */
    public static void main(String[] args) {
        DAOUsuario     dao    = getInstance();
        XMLEventReader lector = dao.obtenerXmlEventReader();
        dao.cargarArchivo(lector);

        for (Usuario usuario : dao.usuarios()) {
            System.out.println(usuario);
        }


    }

    /**
     * Método que devuelve la instancia de DAOUsuario
     *
     * @return instancia de DAOUsuario
     */
    public static DAOUsuario getInstance() {
        if (instance == null) {
            instance = new DAOUsuario();
        }
        return instance;
    }

    /**
     * Método que devuelve los usuarios en formato de colección
     *
     * @return Colección de usuarios
     */
    private Collection<Usuario> usuarios() {
        return this.usuarios;
    }

    /**
     * Método que lee el DNI de un usuario a partir de un XML. Método auxiliar
     * @param reader Lector de eventos XML
     * @return DNI del usuario
     */
    private int _obtenerDNI(XMLEventReader reader) {
        XMLEvent evento = this.getNextXmlEvent(reader);
        int      DNI    = Integer.parseInt(evento.asCharacters().getData());
        this.logger.trace("DNI: {}", evento.asCharacters().getData());
        return DNI;
    }

    /**
     * Método que extrae el nombre de un usuario a partir de un XML. Método auxiliar
     * @param reader Lector de eventos XML
     * @return Nombre del usuario
     */
    private String _obtenerNombre(XMLEventReader reader) {
        XMLEvent evento = this.getNextXmlEvent(reader);
        String   nombre = evento.asCharacters().getData();
        this.logger.trace("Nombre: {}", nombre);
        return nombre;
    }

    /**
     * Método que extrae el correo de un usuario a partir de un XML. Método auxiliar
     * @param reader Lector de eventos XML
     * @return Correo del usuario
     */
    private String _obtenerCorreo(XMLEventReader reader) {
        XMLEvent evento = this.getNextXmlEvent(reader);
        String   correo = evento.asCharacters().getData();
        this.logger.trace("Correo: {}", correo);
        return correo;
    }

    /**
     * Método que extrae la contraseña de un usuario a partir de un XML. Método auxiliar
     * @param reader Lector de eventos XML
     * @return Contraseña del usuario
     */
    private String _obtenerContrasenha(XMLEventReader reader) {
        XMLEvent evento      = this.getNextXmlEvent(reader);
        String   contrasenha = evento.asCharacters().getData();
        this.logger.trace("Contraseña: {}", contrasenha);
        return contrasenha;
    }

    /**
     * Método que procesa el teléfono de un usuario a partir de un XML. Método auxiliar
     * @param reader Lector de eventos XML
     * @return Teléfono del usuario
     */
    private int _procesarTelefono(@NotNull XMLEventReader reader) {

        XMLEvent evento = this.getNextXmlEvent(reader);

        int telefono;
        if (!evento.toString().matches("^\\d+$")) {
            this.logger.error("Error al leer el archivo: el teléfono no es un número. Se asigna 0");
            return 0;
        }
        telefono = Integer.parseInt(evento.asCharacters().getData());
        this.logger.trace("Teléfono: {}", telefono);
        return telefono;
    }

    /**
     * Método que extrae la dirección de un usuario a partir de un XML. Método auxiliar
     * @param reader Lector de eventos XML
     * @return Dirección del usuario
     */
    private String _obtenerDireccion(XMLEventReader reader) {
        XMLEvent evento    = this.getNextXmlEvent(reader);
        String   direccion = evento.asCharacters().getData();
        this.logger.trace("Dirección: {}", direccion);
        return direccion;
    }

    /**
     * Extrae y procesa la fecha de nacimiento de un usuario a partir de un XML. Método auxiliar
     * @param reader Lector de eventos XML
     * @param fechaNacimiento Array de enteros donde se almacenará la fecha de nacimiento
     */
    private void _obtenerFecha(@NotNull XMLEventReader reader, int[] fechaNacimiento) {

        do {
            if (!reader.hasNext()) {
                throw new LecturaSiguienteEventoException();
            }
            XMLEvent evento = this.getNextXmlEvent(reader);
            if (evento.isEndElement() && evento.asEndElement().getName().getLocalPart().equals(TagsXMLUsuario.TAG_XML_FECHA_NACIMIENTO)) {
                logger.debug("Fin de campo de fecha de nacimiento");
                break;
            }
            if (evento.isStartElement()) {
                logger.debug("Inicio de campo de fecha de nacimiento de tipo: {}", evento.asStartElement().getName().getLocalPart());
                _gestionarDatosEntrantesFecha(reader, evento, fechaNacimiento);
            }
        } while (reader.hasNext());

        this.logger.trace("Fecha de nacimiento: {}", fechaNacimiento);

    }

    /**
     * Crea un usuario y lo añade a la lista de usuarios. Método auxiliar
     * @param DNI DNI del usuario
     * @param nombre Nombre del usuario
     * @param correo Correo del usuario
     * @param contrasenha Contraseña del usuario
     * @param telefono Teléfono del usuario
     * @param direccion Dirección del usuario
     * @param fechaNacimiento Fecha de nacimiento del usuario
     */
    private void _crearYanhadirUsuario(int DNI, String nombre, String correo, String contrasenha, int telefono, String direccion, @NotNull int[] fechaNacimiento) {
        Usuario usr;
        this.logger.trace("Fin de usuario");
        usr = new Usuario(DNI, nombre, correo, contrasenha, telefono, direccion, LocalDate.of(fechaNacimiento[0], fechaNacimiento[1], fechaNacimiento[2]), false);
        usuarios.add(usr);
        this.logger.debug("Usuario añadido: {}", usr);
    }

    /**
     * Método que gestiona los datos entrantes de una fecha. Método auxiliar
     * @param reader Lector de eventos XML
     * @param evento Evento XML
     * @param fecha Array de enteros donde se almacenará la fecha
     */
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
            }

        }
    }

    @Nullable
    @Override
    protected XMLStreamWriter obtenerXMLStreamWriter() {
        this.obtenerLogger();

        XMLStreamWriter writer;

        try {
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            writer = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("usuarios.xml"));
        } catch (FileNotFoundException e) {
            this.logger.error("El archivo no ha podido ser encontrado", e);
            throw new SituacionDeRutasInesperadaException("El archivo no ha podido ser encontrado");
        } catch (XMLStreamException e) {
            this.logger.error("Error al volcar el archivo", e);
            throw new SituacionDeRutasInesperadaException("Error al volcar el archivo");
        }

        return writer;

    }

    @Override
    protected void guardarArchivo(@NotNull XMLStreamWriter writer) {

        escribirAperturaCabeceraArchivoXML(writer, ConstantesGeneral.FICHERO_USUARIO);

        try {

            for (Usuario usuario : usuarios) {

                escribirUsuario(writer, usuario);
            }

            writer.writeEndElement();

        } catch (XMLStreamException e) {
            this.logger.error("Error al escribir en el archivo", e);
        }

        try {
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            this.logger.error("Error al cerrar el archivo", e);
        }
    }

    private void escribirUsuario(@NotNull XMLStreamWriter writer, @NotNull Usuario usuario) throws XMLStreamException {
        writer.writeStartElement(TagsXMLUsuario.TAG_XML_USUARIO);

        escribirElemento(writer, TagsXMLUsuario.TAG_XML_DNI, String.valueOf(usuario.DNI()));
        escribirElemento(writer, TagsXMLUsuario.TAG_XML_NOMBRE, usuario.nombre());
        escribirElemento(writer, TagsXMLUsuario.TAG_XML_CORREO, usuario.correo());
        escribirElemento(writer, TagsXMLUsuario.TAG_XML_CONTRASENHA, usuario.contrasenha());
        escribirElemento(writer, TagsXMLUsuario.TAG_XML_TELEFONO, String.valueOf(usuario.telefono()));
        escribirElemento(writer, TagsXMLUsuario.TAG_XML_DIRECCION, usuario.direccion());

        writer.writeStartElement(TagsXMLUsuario.TAG_XML_FECHA_NACIMIENTO);
        escribirElemento(writer, "dia", String.valueOf(usuario.fechaNacimiento().getDayOfMonth()));
        escribirElemento(writer, "mes", String.valueOf(usuario.fechaNacimiento().getMonthValue()));
        escribirElemento(writer, "anho", String.valueOf(usuario.fechaNacimiento().getYear()));
        writer.writeEndElement();

        writer.writeEndElement();
    }

    @Override
    protected XMLEventReader obtenerXmlEventReader() {
        this.obtenerLogger();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlReader = null;
        try {
            xmlReader = xmlInputFactory.createXMLEventReader(new FileInputStream("usuarios.xml"));
        } catch (FileNotFoundException | XMLStreamException e) {
            this.logger.error("Error al leer el archivo: ", e);
        }
        return xmlReader;

    }

    @Override
    protected void cargarArchivo(@NotNull XMLEventReader reader) {
        String nombre   = "err", correo = "err", contrasenha = "err", direccion = "err";
        int    telefono = 0, DNI = 0;
        int[]  fechaNacimiento = new int[3];

        while (reader.hasNext()) {
            XMLEvent evento = this.getNextXmlEvent(reader);

            if (evento.isStartElement()) {
                String nombreElemento = evento.asStartElement().getName().getLocalPart();
                if (nombreElemento.equals(ConstantesGeneral.FICHERO_USUARIO)) {
                    this.logger.trace("Inicio de archivo");
                    this.getNextXmlEvent(reader);
                    continue;
                }

                try {
                    switch (nombreElemento) {
                        case TagsXMLUsuario.TAG_XML_USUARIO -> this.logger.trace("Inicio de usuario");
                        case TagsXMLUsuario.TAG_XML_DNI -> DNI = _obtenerDNI(reader);
                        case TagsXMLUsuario.TAG_XML_NOMBRE -> nombre = _obtenerNombre(reader);
                        case TagsXMLUsuario.TAG_XML_CORREO -> correo = _obtenerCorreo(reader);
                        case TagsXMLUsuario.TAG_XML_CONTRASENHA -> contrasenha = _obtenerContrasenha(reader);
                        case TagsXMLUsuario.TAG_XML_TELEFONO -> telefono = _procesarTelefono(reader);
                        case TagsXMLUsuario.TAG_XML_DIRECCION -> direccion = _obtenerDireccion(reader);
                        case TagsXMLUsuario.TAG_XML_FECHA_NACIMIENTO -> _obtenerFecha(reader, fechaNacimiento);
                        default -> throw new IllegalStateException("Etiqueta desconocida: " + nombreElemento);
                    }
                } catch (NumberFormatException e) {
                    this.logger.error("Error al leer el archivo", e);
                }
            }
            if (evento.isEndElement() && evento.asEndElement().getName().getLocalPart().equals(TagsXMLUsuario.TAG_XML_USUARIO)) {
                _crearYanhadirUsuario(DNI, nombre, correo, contrasenha, telefono, direccion, fechaNacimiento);
            }
        }
    }

    /**
     * Método que autentica a un usuario
     *
     * @param email          Correo del usuario
     * @param hashedPassword Contraseña del usuario
     * @return true si el usuario es autenticado, false en caso contrario
     */
    boolean autenticar(String email, String hashedPassword) {
        //logar la contraseña descifrada :: cifrada (DEBUG)
//        this.logger.debug("Contraseña cifrada: {} -> descifrada: {}", hashedPassword, util.criptograficos.descifrar(hashedPassword));

        comprobarHayUsuariosEnOrigen();
        // Había pensado en comprobar si el correo existe, pero no sé si es muy eficiente, ya que se recorrería la lista dos veces.
        if (this.usuarios.stream().anyMatch(usr ->/* usr.correo().equals(email) &&*/ usr.contrasenha().equals(hashedPassword))) {
            this.logger.info("Usuario autenticado: {}", email);
            return true;
        }
        this.logger.warn("Usuario no autenticado: {}", email);
        return false;
    }

    private boolean comprobarHayUsuariosEnOrigen() {
        if (usuarios.isEmpty()) {
            this.logger.warn("No hay usuarios registrados");
            return true;
        }
        return false;
    }

    Usuario encontrarUsuarioPorDNI(String usuarioDNI) throws UsuarioNoEncontradoException {
        if (comprobarHayUsuariosEnOrigen()) {
            throw new noHayUsuariosRegistradosException();
        }
        Usuario usr = this.usuarios.stream().filter(u -> (u.DNI() == Integer.parseInt(usuarioDNI))).findFirst().orElse(null);
        if (usr != null) {
            this.logger.info("Usuario encontrado: {}", usuarioDNI);
        }
        else {
            this.logger.warn("Usuario no encontrado: {}", usuarioDNI);
            throw new UsuarioNoEncontradoException("Usuario no encontrado: " + usuarioDNI);
        }
        return usr;
    }

    /**
     * Método que obtiene un usuario
     *
     * @param email Correo del usuario
     * @return Usuario si existe, null en caso contrario
     */
    Usuario encontrarUsuarioPorEmail(String email) {
        if (comprobarHayUsuariosEnOrigen()) {
            throw new noHayUsuariosRegistradosException();
        }
        Usuario usr = this.usuarios.stream().filter(u -> u.correo().equals(email)).findFirst().orElse(null);
        if (usr != null) {
            this.logger.info("Usuario encontrado: {}.", email);
        }
        else {
            this.logger.warn("Usuario no encontrado: {}, ¿Está bien escrito?", email);
        }
        return usr;
    }


    void actualizarUsuario(@NonNls String correoAntiguo, Usuario usuario) throws UsuarioNoEncontradoException {
        Usuario usuarioActualizar = this.usuarios.stream().filter(u -> u.correo().equals(correoAntiguo)).findFirst().orElse(null);
        if (usuarioActualizar == null) {
            this.logger.warn("No se ha encontrado el usuario con correo: {}", correoAntiguo);
            throw new UsuarioNoEncontradoException("No se ha encontrado el usuario con correo: " + correoAntiguo);
        }

        logger.debug("Usuario a actualizar: {}", usuarioActualizar);
        usuarioActualizar.actualizarDatos(usuario.correo(), usuario.direccion(), usuario.telefono());
        logger.debug("Usuario actualizado: {}", usuarioActualizar);
    }
}
