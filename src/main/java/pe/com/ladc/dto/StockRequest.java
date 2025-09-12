package pe.com.ladc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Stock operation request")
public class StockRequest {
    @Schema(example = "1", description = "Game ID")
    private Long gameId;

    @Schema(example = "2", description = "Requested quantity")
    private Integer quantity;
}
