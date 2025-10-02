package pe.com.ladc.entity;

import org.junit.jupiter.api.Test;
import pe.com.ladc.enums.GameCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GameEntityTest {

    private Stock buildStock(int total, int reserved) {
        return Stock.builder()
                .id(1L)
                .totalStock(total)
                .reservedStock(reserved)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Game buildGameWithStock(int total, int reserved) {
        Stock stock = buildStock(total, reserved);
        Game g = Game.builder()
                .id(1L)
                .title("FIFA 25")
                .category(GameCategory.SPORTS)
                .description("Football simulation")
                .price(BigDecimal.valueOf(59.99))
                .releaseDate(LocalDate.of(2025, 9, 30))
                .active(true)
                .stock(stock)
                .build();
        stock.setGame(g);
        return g;
    }

    @Test
    void testIsAvailableForSale() {
        Game game = buildGameWithStock(10, 0);
        assertTrue(game.isAvailableForSale());

        game.getStock().setReservedStock(10);
        assertFalse(game.isAvailableForSale());

        game.setStock(null);
        assertFalse(game.isAvailableForSale());
    }

    @Test
    void testChangePrice_validAndInvalid() {
        Game game = buildGameWithStock(10, 0);
        game.changePrice(BigDecimal.valueOf(99.99));
        assertEquals(BigDecimal.valueOf(99.99), game.getPrice());

        assertThrows(IllegalArgumentException.class, () -> game.changePrice(BigDecimal.ZERO));
    }

    @Test
    void testReserveAndReleaseStock() {
        Game game = buildGameWithStock(10, 0);

        game.reserveStock(5);
        assertEquals(5, game.getStock().getReservedStock());

        game.releaseStock(3);
        assertEquals(2, game.getStock().getReservedStock());
    }

    @Test
    void testConfirmSale() {
        Game game = buildGameWithStock(10, 5);

        game.confirmSale(5);
        assertEquals(0, game.getStock().getReservedStock());
        assertEquals(5, game.getStock().getTotalStock());
    }
}
