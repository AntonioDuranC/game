package pe.com.ladc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import pe.com.ladc.entity.Game;

import java.math.BigDecimal;

@Schema(description = "Order Items")
@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference // evita ciclos al serializar
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @JsonBackReference // evita ciclos al serializar
    private Game game;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    // -------------------
    // Métodos de negocio
    // -------------------

    /**
     * Actualiza la cantidad del ítem.
     * Solo se permite si la cantidad es mayor a cero.
     */
    public void updateQuantity(Integer newQuantity) {
        if (newQuantity == null || newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.quantity = newQuantity;
    }

    /**
     * Calcula el subtotal del ítem (cantidad × precio).
     */
    public BigDecimal calculateSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Valida que el precio sea positivo.
     */
    public void validatePrice() {
        if (this.price == null || this.price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
    }

    /**
     * Cambia el juego asociado al ítem.
     * No se permite si la orden ya está cancelada o entregada.
     */
    public void changeGame(Game newGame) {
        if (!order.validDeleteItem()) {
            throw new IllegalStateException(
                    "Cannot change game for orders in status: " + order.getStatus()
            );
        }
        this.game = newGame;
    }

    /**
     * Verifica si el ítem puede ser eliminado según el estado de la orden.
     */
    public boolean canBeDeleted() {
        return order.validDeleteItem();
    }
}
