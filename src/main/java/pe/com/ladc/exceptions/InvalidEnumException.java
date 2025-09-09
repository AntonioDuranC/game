package pe.com.ladc.exceptions;

/**
 * Excepción personalizada para indicar que una categoría de juego no es válida.
 */
public class InvalidEnumException extends RuntimeException {

    public InvalidEnumException(String message) {
        super(message);
    }
}
