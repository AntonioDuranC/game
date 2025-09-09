package pe.com.ladc.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;
import pe.com.ladc.enums.Category;


/**
 * Representa un videojuego dentro del sistema de ventas online.
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Game model")
@Entity
@Table(name = "games") // ⚡ recomendable para mayor control del nombre en DB
@Data
@Builder // ⚡ añade patrón Builder para crear instancias más legibles
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
    private Long id; // ⚡ mejor usar Long (wrapper), soporta null antes de persistir

    @Schema(description = "Title of the game", example = "Frostpunk")
    @Column(nullable = false, length = 100) // ⚡ evita nombres nulos, limita tamaño
    private String title;

    @Enumerated(EnumType.STRING) // Guarda el valor como texto en la BD
    @Schema(description = "Category of game", example = "RPG")
    private Category category;

    @Schema(description = "Description of the game", example = "A survival strategy game set in a frozen world")
    @Column(length = 500) // ⚡ limite para no ocupar mucho espacio
    private String description;

    @Schema(description = "Price of the game", example = "59.99")
    @Column(nullable = false, precision = 10, scale = 2) // ⚡ precisión y escala monetaria
    private BigDecimal price;

    @Schema(description = "Stock available", example = "15")
    @Column(nullable = false)
    private Integer stock;

    @Schema(description = "Release date of the game", example = "2023-06-15")
    @Column(name = "release_date")
    private LocalDate releaseDate;


    // ⚡ Métodos de conveniencia
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
