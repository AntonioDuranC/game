package pe.com.ladc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.ladc.enums.CurrencyType;
import pe.com.ladc.enums.PaymentMethod;
import pe.com.ladc.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO used for sending payment info in API responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private Long orderId;
    private BigDecimal amount;           // Monto original
    private CurrencyType currency;       // 💰 Moneda del pago (PEN / USD)
    private BigDecimal convertedAmount;  // 💵 Monto convertido a soles (si aplica)
    private BigDecimal exchangeRate;     // 💱 Tipo de cambio aplicado (si aplica)
    private LocalDateTime exchangeDate;  // 📅 Fecha del tipo de cambio
    private LocalDateTime paymentDate;
    private PaymentMethod method;
    private PaymentStatus status;
}
