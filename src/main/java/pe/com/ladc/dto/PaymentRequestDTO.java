package pe.com.ladc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.ladc.enums.PaymentMethod;
import java.math.BigDecimal;

/**
 * DTO used for creating a new payment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private Long orderId;
    private BigDecimal amount;
    private PaymentMethod method;
}
