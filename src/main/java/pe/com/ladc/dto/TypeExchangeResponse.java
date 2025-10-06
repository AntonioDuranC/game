package pe.com.ladc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeExchangeResponse {

    @JsonProperty("fecha")
    private String fecha;

    @JsonProperty("sunat")
    private Double sunat;

    @JsonProperty("compra")
    private Double compra;

    @JsonProperty("venta")
    private Double venta;
}