package pe.com.ladc.strategy.discount;

import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.entity.Discount;
import pe.com.ladc.strategy.discount.impl.*;

@ApplicationScoped
public class DiscountStrategyContext {

    public DiscountStrategy resolve(String type) {
        if (type == null) return new NoDiscountStrategy();

        return switch (type.toUpperCase()) {
            case "PERCENTAGE" -> new PercentageDiscountStrategy();
            case "FIXED" -> new FixedAmountDiscountStrategy();
            case "PAYMENT_METHOD" -> new PaymentMethodDiscountStrategy();
            default -> new NoDiscountStrategy();
        };
    }
}
