package pe.com.ladc;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pe.com.ladc.entity.Game;
import pe.com.ladc.repository.GameRepository;


@QuarkusTest
class GameRepositoryTest {

    @Inject
    GameRepository repository;

    @Test
    @TestTransaction
    void testFindById() {
        // 1. Arrange: Create and persist a new Games entity
        Game game = new Game();
        game.setTitle("The Witcher 3: Wild Hunt");
        game.setActive(true);
        repository.persist(game);

        // 2. Act: Call the method you want to test with the newly created entity's ID
        Game foundGame = repository.findById(game.getId());

        // 3. Assert: Verify the result
        Assertions.assertNotNull(foundGame, "The game should not be null.");
        Assertions.assertEquals(game.getTitle(), foundGame.getTitle(), "Titles should match.");
    }
}