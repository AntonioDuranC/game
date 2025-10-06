package pe.com.ladc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g. "Black Friday", "Visa Promo", etc.

    @Column(nullable = false)
    private String type; // PERCENTAGE, FIXED, PAYMENT_METHOD

    @Column(precision = 10, scale = 2)
    private BigDecimal value; // porcentaje o monto fijo

    @Column(name = "payment_method")
    private String paymentMethod; // opcional (ej. VISA, PAYPAL)

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(nullable = false)
    private boolean active;
}
