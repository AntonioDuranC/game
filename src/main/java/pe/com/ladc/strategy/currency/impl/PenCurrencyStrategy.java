package pe.com.ladc.strategy.currency.impl;

import java.math.BigDecimal;
import pe.com.ladc.dto.TypeExchangeResponse;
import pe.com.ladc.strategy.currency.CurrencyStrategy;

public class PenCurrencyStrategy implements CurrencyStrategy {

    @Override
    public BigDecimal convertAmount(BigDecimal amount, TypeExchangeResponse exchangeRate) {
        return amount; // sin conversión
    }

    @Override
    public String getCurrencyCode() {
        return "PEN";
    }
}
