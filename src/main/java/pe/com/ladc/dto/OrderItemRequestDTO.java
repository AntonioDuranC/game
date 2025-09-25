package pe.com.ladc.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequestDTO {
    private Long gameId;
    private Integer quantity;
    private BigDecimal price;
}
