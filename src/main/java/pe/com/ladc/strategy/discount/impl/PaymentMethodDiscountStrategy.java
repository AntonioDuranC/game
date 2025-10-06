package pe.com.ladc.strategy.discount.impl;

import java.math.BigDecimal;
import pe.com.ladc.dto.PaymentRequestDTO;
import pe.com.ladc.entity.Discount;
import pe.com.ladc.strategy.discount.DiscountStrategy;

public class PaymentMethodDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal applyDiscount(BigDecimal amount, PaymentRequestDTO request, Discount discount) {
        if (discount == null || discount.getPaymentMethod() == null) return amount;

        if (discount.getPaymentMethod().equalsIgnoreCase(request.getMethod().name())) {
            BigDecimal discountValue = amount.multiply(discount.getValue().divide(BigDecimal.valueOf(100)));
            return amount.subtract(discountValue);
        }
        return amount;
    }
}
