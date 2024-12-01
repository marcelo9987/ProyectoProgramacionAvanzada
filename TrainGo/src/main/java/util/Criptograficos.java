package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Generated;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Criptograficos {
    private static final Logger logger = LoggerFactory.getLogger(Criptograficos.class);

    private static final String ALGORITMO = "DES";
    private static final String CLAVE = "82837445";

    /**
     * Método que obtiene una clave secreta a partir de una cadena de texto.
     *
     * @param clave Cadena de texto a partir de la cual se obtendrá la clave secreta.
     * @return Clave secreta.
     */
    @NotNull
    @Contract("_ -> new")
    private static SecretKey obtenerClave(@NotNull String clave) {
        if (clave.length() < 8) {
            LoggerFactory.getLogger(Criptograficos.class).error("La clave debe tener al menos 8 caracteres.");
            throw new IllegalArgumentException("La clave debe tener al menos 8 caracteres.");
        }
        return new SecretKeySpec(clave.getBytes(), ALGORITMO);
    }

    /**
     * Método que cifra un texto.
     *
     * @param texto Texto a cifrar. Utiliza la clave secreta definida en la clase.
     * @return Texto cifrado.
     * @apiNote Utiliza el algoritmo DES. La idea era poder cambiar el algoritmo, pero se me ha complicado.
     * @see #descifrar(String)
     */
    @Nullable
    public static String cifrar(@NotNull String texto) {

        byte[] mensajeEntrada = texto.getBytes();
        try {
            ObtenerEncriptadorYCalcularClave encriptadorYClave = obtenerEncriptadorYCalcularClave();
            encriptadorYClave.traductor().init(Cipher.ENCRYPT_MODE, encriptadorYClave.claveSecreta());
            byte[] claveCifrada = encriptadorYClave.traductor().doFinal(mensajeEntrada);
            return new String(Base64.getEncoder().encode(claveCifrada));
        } catch (InvalidKeyException e) {
            // En la documentación pone que esta excepción no se lanza, pero por si acaso...
            logger.error("Clave Mal Formada, ¿Están bien hechos los cálculos? :(");
            System.exit(-1);
        } catch (IllegalBlockSizeException ibe) {
            // Esto ocurre cuando el tamaño del bloque es incorrecto.
            logger.error(" El tamaño del bloque es incorrecto. ¿Has hecho bien los cálculos?");
        } catch (Exception e) {
            // Estos son errores que solo deberían ocurrir en caso de desencriptación, por lo que los ignoramos.
            logger.error("Error desconocido: " + e.getMessage());
        }
        return null;
    }

    /**
     * Método que descifra un texto.
     *
     * @param textoCifrado Texto cifrado.
     * @return Texto descifrado.
     * @see #cifrar(String)
     */
    @Nullable
    public static String descifrar(@NotNull String textoCifrado) {
        try {
            ObtenerEncriptadorYCalcularClave encriptadorYClave = obtenerEncriptadorYCalcularClave();
            encriptadorYClave.traductor().init(Cipher.DECRYPT_MODE, encriptadorYClave.claveSecreta());
            byte[] textoDescifrado = encriptadorYClave.traductor().doFinal(Base64.getDecoder().decode(textoCifrado.getBytes()));
            return new String(textoDescifrado);
        } catch (InvalidKeyException e) {
            // En la documentación pone que esta excepción no se lanza, pero por si acaso...
            logger.error("Clave Mal Formada, ¿Están bien hechos los cálculos? :(");
            System.exit(-1);
        } catch (IllegalBlockSizeException ibe) {
            // Esto ocurre cuando el tamaño del bloque es incorrecto.
            logger.error(" El tamaño del bloque es incorrecto. ¿Has hecho bien los cálculos?");
        } catch (BadPaddingException bpe) {
            // Esto ocurre cuando el padding es incorrecto.
            logger.error("El padding es incorrecto. ¿Has hecho bien los cálculos?");
        } catch (Exception e) {
            // Estos son errores que solo deberían ocurrir en caso de desencriptación, por lo que los ignoramos.
            logger.error("Error desconocido: " + e.getMessage());
        }
        return null;
    }

    @NotNull
    private static ObtenerEncriptadorYCalcularClave obtenerEncriptadorYCalcularClave() {
        try {
            Cipher traductor = Cipher.getInstance(ALGORITMO);
            SecretKey claveSecreta = obtenerClave(CLAVE);
            return new ObtenerEncriptadorYCalcularClave(traductor, claveSecreta);
        } catch (NoSuchAlgorithmException e) {
            // Algoritmo nunca debería ser nulo, descartamos la excepción por ese frente.
            logger.error("Algoritmo no encontrado o no implementado :( " + ALGORITMO);
            System.exit(-1);
        } catch (NoSuchPaddingException e) {
            // Padding nunca debería ser nulo, descartamos la excepción por ese frente.
            logger.error("Padding no implementado :(");
            System.exit(-1);
        }
        logger.error(" Error desconocido al obtener el encriptador y calcular la clave.");
        System.exit(-1);
        return new ObtenerEncriptadorYCalcularClave(null, null);// Nunca se llega aquí.
    }

    @Generated("IntelliJ IDEA REFACTORING")
    private record ObtenerEncriptadorYCalcularClave(Cipher traductor, SecretKey claveSecreta) {
    }


    /**
     * Método que calcula la letra de un DNI
     *
     * @param dniNumber Número del DNI
     * @return Letra del DNI
     */
    public static char calculateDniLetter(int dniNumber) throws IllegalArgumentException {
        if (dniNumber < 0 || dniNumber > 99999999) {
            throw new IllegalArgumentException("El número del DNI no puede ser negativo.");
        }

        Map<Integer, Character> letterMap = new HashMap<>();
        letterMap.put(0, 'T');
        letterMap.put(1, 'R');
        letterMap.put(2, 'W');
        letterMap.put(3, 'A');
        letterMap.put(4, 'G');
        letterMap.put(5, 'M');
        letterMap.put(6, 'Y');
        letterMap.put(7, 'F');
        letterMap.put(8, 'P');
        letterMap.put(9, 'D');
        letterMap.put(10, 'X');
        letterMap.put(11, 'B');
        letterMap.put(12, 'N');
        letterMap.put(13, 'J');
        letterMap.put(14, 'Z');
        letterMap.put(15, 'S');
        letterMap.put(16, 'Q');
        letterMap.put(17, 'V');
        letterMap.put(18, 'H');
        letterMap.put(19, 'L');
        letterMap.put(20, 'C');
        letterMap.put(21, 'K');
        letterMap.put(22, 'E');

        int codigoCaracterResultante = dniNumber % 23;

        return letterMap.get(Integer.valueOf(codigoCaracterResultante)).charValue();
    }
}
