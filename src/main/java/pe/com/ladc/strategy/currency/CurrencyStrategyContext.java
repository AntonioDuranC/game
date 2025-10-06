package pe.com.ladc.strategy.currency;

import jakarta.enterprise.context.ApplicationScoped;
import pe.com.ladc.enums.CurrencyType;
import pe.com.ladc.strategy.currency.impl.PenCurrencyStrategy;
import pe.com.ladc.strategy.currency.impl.UsdCurrencyStrategy;

@ApplicationScoped
public class CurrencyStrategyContext {

    public CurrencyStrategy resolve(CurrencyType type) {
        return switch (type) {
            case PEN -> new PenCurrencyStrategy();
            case USD -> new UsdCurrencyStrategy();
            default -> throw new IllegalArgumentException("Unsupported currency: " + type);
        };
    }
}
