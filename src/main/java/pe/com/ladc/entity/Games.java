package pe.com.ladc.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;
import pe.com.ladc.enums.GameCategory;


/**
 * Representa un videojuego dentro del sistema de ventas online.
 */
@Schema(description = "Game")
@Entity
@Table(name = "games")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Games extends PanacheEntityBase {

    @Schema(description = "Game id", example = "1")
    @Id
    @GeneratedValue(generator = "games_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "games_id_seq",
            sequenceName = "games_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Schema(description = "Title of the game", example = "Frostpunk")
    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Category of game", example = "RPG")
    private GameCategory category;

    @Schema(description = "Description of the game", example = "A survival strategy game set in a frozen world")
    @Column(length = 500)
    private String description;

    @Schema(description = "Price of the game", example = "59.99")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Schema(description = "Stock available", example = "15")
    @Column(nullable = false)
    private Integer stock;

    @Schema(description = "Release date of the game", example = "2023-06-15")
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Schema(description = "Indicates whether the game is active", example = "true")
    @Column(name = "active")
    private Boolean active;

    public boolean isInStock() {
        return stock != null && stock > 0;
    }

    public void decreaseStock(int quantity) {
        if (stock == null || stock < quantity) {
            throw new IllegalArgumentException("Not enough stock available");
        }
        stock -= quantity;
    }
}
