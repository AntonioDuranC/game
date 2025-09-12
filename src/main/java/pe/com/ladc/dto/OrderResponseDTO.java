package pe.com.ladc.dto;

import lombok.Builder;
import lombok.Data;
import pe.com.ladc.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private OrderStatus status;
}
