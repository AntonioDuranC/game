package pe.com.ladc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pe.com.ladc.enums.GameCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "games")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    private GameCategory category;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "active")
    private Boolean active;

    // Métodos de negocio:

    /**
     * Desactivar el juego (borrado lógico).
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Activar el juego.
     */
    public void activate() {
        this.active = true;
    }


    /**
     * Actualizar la descripción del juego.
     */
    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * Cambiar el precio del juego.
     */
    public void changePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        this.price = newPrice;
    }

}
