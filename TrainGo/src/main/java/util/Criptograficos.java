package util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Criptograficos {
    private static final String ALGORITMO = "DES";
    private static final String CLAVE = "82837445";

    public static SecretKey obtenerClave(String clave) {
        return new SecretKeySpec(clave.getBytes(), ALGORITMO);
    }

    public static String cifrar(String texto) {

        byte[] mensajeEntrada = texto.getBytes();
        try {
            Cipher encriptador = Cipher.getInstance(ALGORITMO);
            SecretKey claveSecreta = obtenerClave(CLAVE);
            encriptador.init(Cipher.ENCRYPT_MODE, claveSecreta);
            byte[] claveCifrada = encriptador.doFinal(mensajeEntrada);
            return new String(Base64.getEncoder().encode(claveCifrada));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String descifrar(String textoCifrado) {
        try {
            Cipher descifrador = Cipher.getInstance(ALGORITMO);
            SecretKey claveSecreta = obtenerClave(CLAVE);
            descifrador.init(Cipher.DECRYPT_MODE, claveSecreta);
            byte[] textoDescifrado = descifrador.doFinal(Base64.getDecoder().decode(textoCifrado.getBytes()));
            return new String(textoDescifrado);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * Método que calcula la letra de un DNI
     *
     * @param dniNumber Número del DNI
     * @return Letra del DNI
     */
    public char calculateDniLetter(int dniNumber) {
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

        return letterMap.get(codigoCaracterResultante);
    }
}
