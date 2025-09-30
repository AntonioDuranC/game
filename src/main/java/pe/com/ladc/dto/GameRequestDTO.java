package pe.com.ladc.dto;

import lombok.*;
import pe.com.ladc.enums.GameCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameRequestDTO {

    private String title;
    private GameCategory category;
    private String description;
    private BigDecimal price;
    private LocalDate releaseDate;

    // Opcional: solo si quieres que el cliente pueda setear stock inicial
    private Integer stockQuantity;
}
