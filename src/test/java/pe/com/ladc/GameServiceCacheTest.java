package pe.com.ladc;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.com.ladc.entity.Game;
import pe.com.ladc.enums.GameCategory;
import pe.com.ladc.service.GameService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GameServiceCacheTest {

    @Inject
    GameService gameService;

    @Inject
    CacheManager cacheManager;

    private Cache gameCache;

    @BeforeEach
    void setup() {
        gameCache = cacheManager.getCache("game-cache")
                .orElseThrow(() -> new IllegalStateException("Cache 'game-cache' no existe"));
        // limpiar cache antes de cada test
        gameCache.invalidateAll().await().indefinitely();
    }

    @Test
    void testCacheStoresAndInvalidates() {
        Long gameId = 1L;

        // Al inicio no debe haber nada en cache
        var cachedBefore = gameCache.get(gameId, k -> null).await().indefinitely();
        assertNull(cachedBefore, "El cache debe estar vacío para esa key");

        // Crear juego
        Game game = Game.builder()
                .id(1L)
                .title("Elden Ring - GOTY")
                .category(GameCategory.RPG)
                .description("Updated desc")
                .price(new BigDecimal("79.99"))
                .releaseDate(LocalDate.of(2023, 1, 1))
                .build();

        gameService.createGame(game);

        // Ahora el cache debería tener algo para esa key
        var cachedAfter = gameCache.get(gameId, k -> null).await().indefinitely();
        assertNotNull(cachedAfter, "El juego debería estar en cache después de crearlo");

        // Invalidate → debe volver a null
        gameCache.invalidate(gameId).await().indefinitely();
        var cachedAfterInvalidate = gameCache.get(gameId, k -> null).await().indefinitely();
        assertNull(cachedAfterInvalidate, "El cache debería estar vacío tras invalidar la key");
    }
}
