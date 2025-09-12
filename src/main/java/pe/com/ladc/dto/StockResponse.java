package pe.com.ladc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Stock operation response")
public class StockResponse {
    private boolean success;
    private String message;
    private Integer availableStock;
}
