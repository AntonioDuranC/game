package pe.com.ladc.strategy.discount.impl;

import java.math.BigDecimal;
import pe.com.ladc.dto.PaymentRequestDTO;
import pe.com.ladc.entity.Discount;
import pe.com.ladc.strategy.discount.DiscountStrategy;

public class FixedAmountDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal applyDiscount(BigDecimal amount, PaymentRequestDTO request, Discount discount) {
        if (discount == null || discount.getValue() == null) return amount;
        BigDecimal result = amount.subtract(discount.getValue());
        return result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result;
    }
}
