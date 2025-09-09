package pe.com.ladc.exception;

/**
 * Excepción personalizada para indicar que una categoría de juego no es válida.
 */
public class InvalidCategoryException extends RuntimeException {

    public InvalidCategoryException(String category) {
        super("Invalid category: " + category +
                ". Please use one of the allowed values.");
    }
}
