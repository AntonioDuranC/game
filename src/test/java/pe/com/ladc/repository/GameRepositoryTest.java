package pe.com.ladc.repository;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import pe.com.ladc.entity.Game;
import pe.com.ladc.enums.GameCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest // ✅ aquí sí necesitamos levantar Quarkus
class GameRepositoryTest {

    @Inject
    GameRepository repository; // ✅ inyectamos el repo real

    @BeforeEach
    @Transactional
    void clearTables() {
        repository.deleteAll();
    }

    private Game buildEntity(String title) {

        return Game.builder()
                .title(title)
                .category(GameCategory.SPORTS)
                .description("Simulation")
                .price(BigDecimal.valueOf(59.99))
                .releaseDate(LocalDate.of(2025, 9, 30))
                .active(true)
                .build();
    }

    @Test
    @TestTransaction // ✅ rollback automático
    void testPersistAndFindById() {
        Game game = buildEntity("FIFA 25");
        repository.persist(game);

        assertNotNull(game.getId()); // ID generado

        Game found = repository.findById(game.getId());
        assertNotNull(found);
        assertEquals("FIFA 25", found.getTitle());
    }

    @Test
    @TestTransaction
    void testDeleteGame() {
        Game game = buildEntity("NBA 2K25");
        repository.persist(game);

        Long id = game.getId();
        assertNotNull(id);

        repository.deleteById(id);

        Game deleted = repository.findById(id);
        assertNull(deleted);
    }

    @Test
    @TestTransaction
    void testUpdateGame() {
        Game game = buildEntity("Gran Turismo 7");
        repository.persist(game);

        Long id = game.getId();
        assertNotNull(id);

        Game found = repository.findById(id);
        assertEquals("Gran Turismo 7", found.getTitle());

        // Update
        found.setTitle("Gran Turismo 8");
        repository.persist(found);

        Game updated = repository.findById(id);
        assertEquals("Gran Turismo 8", updated.getTitle());
    }
}
