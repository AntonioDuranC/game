package pe.com.ladc.strategy.discount;

import java.math.BigDecimal;
import pe.com.ladc.entity.Discount;
import pe.com.ladc.dto.PaymentRequestDTO;

public interface DiscountStrategy {
    BigDecimal applyDiscount(BigDecimal amount, PaymentRequestDTO request, Discount discount);
}
