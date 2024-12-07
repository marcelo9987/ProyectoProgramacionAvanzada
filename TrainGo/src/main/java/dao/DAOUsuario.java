package dao;

import aplicacion.Usuario;
import aplicacion.excepciones.SituacionDeRutasInesperadaException;
import aplicacion.excepciones.UsuarioNoEncontradoException;
import aplicacion.excepciones.noHayUsuariosRegistradosException;
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

public class DAOUsuario extends AbstractDAO {

    private static DAOUsuario    instance = null;
    private final  List<Usuario> usuarios;

    private DAOUsuario() {
        super();
        this.obtenerLogger();
        this.usuarios = new ArrayList<>();
    }

    public static void main(String[] args) {
        DAOUsuario dao = getInstance();
        XMLEventReader xmlEvtRdr_lector = dao.obtenerXmlEventReader();
        dao.cargarArchivo(xmlEvtRdr_lector);

        for (Usuario usuario : dao.usuarios()) {
            System.out.println(usuario);
        }

//        Usuario usr = new Usuario(1, "Pepe", "pepef@incibe.cdi.net", "1234", 666666666, "Calle Falsa 123", new Date(), true);

//        dao.addUser(usr);

//        dao.save();
    }

    public static DAOUsuario getInstance() {
        if (instance == null) {
            instance = new DAOUsuario();
        }
        return instance;
    }

    private Collection<Usuario> usuarios() {
        return this.usuarios;
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

        try {
            writer.writeStartDocument();

            writer.writeStartElement("usuarios");

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
        writer.writeStartElement("usuario");

        escribirElemento(writer, "DNI", String.valueOf(usuario.DNI()));
        escribirElemento(writer, "nombre", usuario.nombre());
        escribirElemento(writer, "correo", usuario.correo());
        escribirElemento(writer, "contrasenha", usuario.contrasenha());
        escribirElemento(writer, "telefono", String.valueOf(usuario.telefono()));
        escribirElemento(writer, "direccion", usuario.direccion());

        writer.writeStartElement("fechaNacimiento");
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
        } catch (Exception e) {
            this.logger.error("Error al leer el archivo: ", e);
        }
        return xmlReader;

    }


    @Override
    protected void cargarArchivo(@NotNull XMLEventReader reader) {
        Usuario usr;
        String nombre          = "err";
        String correo          = "err";
        String contrasenha     = "err";
        int    telefono        = 0;
        String direccion       = "err";
        int[]  fechaNacimiento = new int[3];
        int    DNI             = 0;

        while (reader.hasNext()) {
            XMLEvent evento;
            evento = this.getNextXmlEvent(reader);

            if (evento.isStartElement()) {
                try {
                    switch (evento.asStartElement().getName().getLocalPart()) {
                        case "usuario":
                            this.logger.trace("Inicio de usuario");
                            break;
                        case "DNI":
                            evento = this.getNextXmlEvent(reader);
                            DNI = Integer.parseInt(evento.asCharacters().getData());
                            this.logger.trace("DNI: {}", evento.asCharacters().getData());
                            break;
                        case "nombre":
                            evento = this.getNextXmlEvent(reader);
                            nombre = evento.asCharacters().getData();
                            this.logger.trace("Nombre: {}", nombre);
                            break;
                        case "correo":
                            evento = this.getNextXmlEvent(reader);
                            correo = evento.asCharacters().getData();
                            this.logger.trace("Correo: {}", correo);
                            break;
                        case "contrasenha":
                            evento = this.getNextXmlEvent(reader);
                            contrasenha = evento.asCharacters().getData();
                            this.logger.trace("Contraseña: {}", contrasenha);
                            break;
                        case "telefono":
                            if (!reader.peek().isCharacters()) {
                                telefono = 0;
                                this.logger.error("Error al leer el archivo: el teléfono no es un número. Se asigna 0");
                                break;
                            }
                            evento = this.getNextXmlEvent(reader);
                            telefono = Integer.parseInt(evento.asCharacters().getData());
                            this.logger.trace("Teléfono: {}", telefono);
                            break;
                        case "direccion":
                            evento = this.getNextXmlEvent(reader);
                            direccion = evento.asCharacters().getData();
                            this.logger.trace("Dirección: {}", direccion);
                            break;
                        case "fechaNacimiento":
                            evento = this.getNextXmlEvent(reader);
                            while (reader.hasNext()) {
                                if (evento.isEndElement() && evento.asEndElement().getName().getLocalPart().equals("fechaNacimiento")) {
                                    break;
                                }
                                if (evento.isStartElement()) {
                                    logger.debug("Inicio de campo de fecha de nacimiento de tipo: {}", evento.asStartElement().getName().getLocalPart());
                                    switch (evento.asStartElement().getName().getLocalPart()) {
                                        case "dia":
                                            evento = this.getNextXmlEvent(reader);
//                                            fechaNacimiento.set(Calendar.DAY_OF_MONTH, Integer.parseInt(evento.asCharacters().getData()));
                                            fechaNacimiento[0] = Integer.parseInt(evento.asCharacters().getData());
                                            this.logger.trace("Día de nacimiento: {}", evento.asCharacters().getData());
                                            break;
                                        case "mes":
                                            evento = this.getNextXmlEvent(reader);
//                                            fechaNacimiento.set(Calendar.MONTH, Integer.parseInt(evento.asCharacters().getData()));
                                            fechaNacimiento[1] = Integer.parseInt(evento.asCharacters().getData());
                                            this.logger.trace("Mes de nacimiento: {}", evento.asCharacters().getData());
                                            break;
                                        case "anho":
                                            evento = this.getNextXmlEvent(reader);
//                                            fechaNacimiento.set(Calendar.YEAR, Integer.parseInt(evento.asCharacters().getData()));
                                            fechaNacimiento[2] = Integer.parseInt(evento.asCharacters().getData());
                                            this.logger.trace("Año de nacimiento: {}", evento.asCharacters().getData());
                                            break;
                                    }
                                }
                                evento = this.getNextXmlEvent(reader);
                            }
                            this.logger.trace("Fecha de nacimiento: {}", fechaNacimiento);
                            break;
                    }
                } catch (Exception e) {
                    this.logger.error("Error al leer el archivo", e);
                }
            }

            if (evento.isEndElement()) {
                if (evento.asEndElement().getName().getLocalPart().equals("usuario")) {
                    this.logger.trace("Fin de usuario");
                    usr = new Usuario
                            (
                                    DNI
                                    , nombre
                                    , correo
                                    , contrasenha
                                    , telefono
                                    , direccion
                                    , LocalDate.of(fechaNacimiento[2], fechaNacimiento[1], fechaNacimiento[0])
                                    , false
                            );
                    usuarios.add(usr);
                    this.logger.debug("Usuario añadido: {}", usr);
                    telefono = 0;

                }
            }
        }
    }


    public void addUser(Usuario usr) {
        this.logger.trace("Añadiendo usuario: {}", usr);
        if (usuarios.contains(usr)) {
            this.logger.warn("El usuario ya existe");
            return;
        }
        usuarios.add(usr);
    }


    /**
     * Método que autentica a un usuario
     *
     * @param email          Correo del usuario
     * @param hashedPassword Contraseña del usuario
     * @return true si el usuario es autenticado, false en caso contrario
     */
    public boolean autenticar(String email, String hashedPassword) {
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

    /**
     * Método que obtiene un usuario
     *
     * @param email Correo del usuario
     * @return Usuario si existe, null en caso contrario
     */
    public Usuario encontrarUsuarioPorEmail(String email) {
        if (comprobarHayUsuariosEnOrigen()) {
            throw new noHayUsuariosRegistradosException();
        }
        Usuario usr = this.usuarios.stream().filter(u -> u.correo().equals(email)).findFirst().orElse(null);
        if (usr != null) {
            this.logger.info("Usuario encontrado: {}", email);
        }
        else {
            this.logger.warn("Usuario no encontrado: {}", email);
        }
        return usr;
    }

    private boolean comprobarHayUsuariosEnOrigen() {
        if (usuarios.isEmpty()) {
            this.logger.warn("No hay usuarios registrados");
            return true;
        }
        return false;
    }

    public void actualizarUsuario(@NonNls String correoAntiguo, Usuario usuario) throws UsuarioNoEncontradoException {
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
