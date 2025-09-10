package pe.com.ladc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // evita enviar nulos
public class ResponseDTO<T> {
    private String message;
    private int statusCode;
    private T data;
    private List<String> errors;
    private LocalDateTime timestamp;

    public ResponseDTO(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseDTO(String message, int statusCode, T data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
