package pe.com.ladc.entity;

import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Game stock management")
@Entity
@Table(name = "game_stock")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "game_id", nullable = false, unique = true)
    private Game game;

    @Column(name = "total_stock", nullable = false)
    private Integer totalStock;

    @Column(name = "reserved_stock", nullable = false)
    private Integer reservedStock;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Transient
    public Integer getAvailableStock() {
        return totalStock - reservedStock;
    }

    /**
     * Add stock (e.g. new inventory received).
     */
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to add must be greater than 0");
        }
        this.totalStock += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Reserve stock (e.g. customer places an order).
     */
    public void reserveStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to reserve must be greater than 0");
        }
        if (quantity > getAvailableStock()) {
            throw new IllegalStateException("Not enough available stock to reserve");
        }
        this.reservedStock += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Release stock (e.g. order canceled or expired).
     */
    public void releaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to release must be greater than 0");
        }
        if (quantity > this.reservedStock) {
            throw new IllegalStateException("Cannot release more than reserved stock");
        }
        this.reservedStock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Confirm stock (e.g. order is paid and finalized).
     * This reduces total stock because the items leave inventory.
     */
    public void confirmStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to confirm must be greater than 0");
        }
        if (quantity > this.reservedStock) {
            throw new IllegalStateException("Cannot confirm more than reserved stock");
        }
        this.reservedStock -= quantity;
        this.totalStock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }
}
