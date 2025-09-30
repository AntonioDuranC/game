package pe.com.ladc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private Stock stock;

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
     * Verificar si el juego está disponible para la venta.
     * Incluye validación de estado y stock.
     */
    public boolean isAvailableForSale() {
        return Boolean.TRUE.equals(this.active)
                && this.stock != null
                && this.stock.getAvailableStock() > 0;
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

    /**
     * Reservar stock cuando un cliente hace un pedido.
     */
    public void reserveStock(int quantity) {
        if (this.stock == null) {
            throw new IllegalStateException("Stock not defined for game " + this.id);
        }
        this.stock.reserveStock(quantity);
    }

    /**
     * Liberar stock cuando se cancela un pedido.
     */
    public void releaseStock(int quantity) {
        if (this.stock == null) {
            throw new IllegalStateException("Stock not defined for game " + this.id);
        }
        this.stock.releaseStock(quantity);
    }

    /**
     * Confirmar stock cuando se concreta una venta.
     */
    public void confirmSale(int quantity) {
        if (this.stock == null) {
            throw new IllegalStateException("Stock not defined for game " + this.id);
        }
        this.stock.confirmStock(quantity);
    }
}
