package pe.com.ladc.entity;

import jakarta.persistence.*;
import lombok.*;
import pe.com.ladc.enums.OrderStatus;
import pe.com.ladc.exception.InvalidOperationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static pe.com.ladc.enums.OrderStatus.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void cancel() {
        if (this.status == OrderStatus.CANCELLED ||
                this.status == OrderStatus.DELIVERED) {
            throw new InvalidOperationException(
                    "Order cannot be cancelled. Current status: " + this.status);
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void updateStatus(OrderStatus newStatus) {
        if (this.status == OrderStatus.CANCELLED) {
            throw new InvalidOperationException("Cancelled orders cannot change status.");
        }
        this.status = newStatus;
    }

    public void validStatusItem() {
        if (this.status != PENDING) {
            throw new InvalidOperationException(
                    "Cannot update item quantity. Current order status: " + this.status
            );
        }
    }

    public boolean validDeleteItem() {
        return switch (this.getStatus()) {
            case ACCEPTED, PROCESSING, SHIPPED, DELIVERED -> false;
            default -> true;
        };
    }
}
