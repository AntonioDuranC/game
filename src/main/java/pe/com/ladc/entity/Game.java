package pe.com.ladc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import pe.com.ladc.enums.GameCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Game")
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

    // Relación 1:1 con Stock
    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonBackReference // 🔹 evita ciclos al serializar
    private Stock stock;
}
