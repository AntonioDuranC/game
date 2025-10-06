package pe.com.ladc.strategy.currency;

import java.math.BigDecimal;
import pe.com.ladc.dto.TypeExchangeResponse;

public interface CurrencyStrategy {
    BigDecimal convertAmount(BigDecimal amount, TypeExchangeResponse exchangeRate);
    String getCurrencyCode();
}
