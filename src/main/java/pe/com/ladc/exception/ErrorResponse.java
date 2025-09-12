package pe.com.ladc.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;             // Código HTTP
    private String error;           // Nombre del error
    private String message;         // Descripción corta
    private LocalDateTime timestamp; // Momento del error
    private String path;            // Endpoint donde ocurrió
}
