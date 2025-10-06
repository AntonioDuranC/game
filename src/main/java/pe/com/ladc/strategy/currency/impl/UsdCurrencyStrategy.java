package pe.com.ladc.strategy.currency.impl;

import java.math.BigDecimal;
import pe.com.ladc.dto.TypeExchangeResponse;
import pe.com.ladc.strategy.currency.CurrencyStrategy;

public class UsdCurrencyStrategy implements CurrencyStrategy {

    @Override
    public BigDecimal convertAmount(BigDecimal amount, TypeExchangeResponse exchangeRate) {
        // Ejemplo: convertir a soles usando el valor de venta
        if (exchangeRate == null || exchangeRate.getVenta() == null) {
            throw new IllegalStateException("Exchange rate data is missing");
        }
        return amount.multiply(BigDecimal.valueOf(exchangeRate.getVenta()));
    }

    @Override
    public String getCurrencyCode() {
        return "USD";
    }
}
