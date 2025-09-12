package pe.com.ladc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import pe.com.ladc.enums.PaymentMethod;
import pe.com.ladc.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    @JsonBackReference // 🔹 evita ciclos al serializar
    private Order order;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public void validStatus(PaymentStatus newStatus) {
        if (this.status == PaymentStatus.CANCELLED ||
                this.status == PaymentStatus.PAID ||
                this.status == PaymentStatus.REFUNDED) {
            throw new IllegalArgumentException(
                    "Cannot change status. Current status is: " + this.status
            );
        }
        this.status = newStatus;
    }
}
