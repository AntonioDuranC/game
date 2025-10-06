package pe.com.ladc.strategy.discount.impl;

import java.math.BigDecimal;
import pe.com.ladc.dto.PaymentRequestDTO;
import pe.com.ladc.entity.Discount;
import pe.com.ladc.strategy.discount.DiscountStrategy;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal applyDiscount(BigDecimal amount, PaymentRequestDTO request, Discount discount) {
        return amount;
    }
}
