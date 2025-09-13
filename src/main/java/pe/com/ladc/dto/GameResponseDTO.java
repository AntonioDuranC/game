package pe.com.ladc.dto;

import lombok.*;
import pe.com.ladc.enums.GameCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResponseDTO {
    private Long id;
    private String title;
    private GameCategory category;
    private String description;
    private BigDecimal price;
    private LocalDate releaseDate;
    private Boolean active;
    private Integer availableStock; // 🔹 Mostramos solo stock disponible, no los campos internos
}
