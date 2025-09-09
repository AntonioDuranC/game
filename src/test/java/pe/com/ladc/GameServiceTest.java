package pe.com.ladc;

import pe.com.ladc.enums.Category;
import pe.com.ladc.entity.Games;
import pe.com.ladc.repository.GamesRepository;
import pe.com.ladc.services.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GamesRepository gamesRepository;

    @Test
    void testFindPaginatedWithoutName() {

        List<Games> mockGames = List.of(Games.builder()
                .title("Cyberpunk 2077")
                .category(Category.RPG)
                .price(new BigDecimal("49.99"))
                .stock(100)
                .releaseDate(LocalDate.of(2020, 12, 10))
                .build());

        Mockito.when(gamesRepository.findPaginated(0, 10, "category", null))
                .thenReturn(mockGames);

        List<Games> result = gameService.findPaginated(0, 10, null);

        assertEquals(1, result.size());
        assertEquals("Game1", result.get(0).getTitle());
        Mockito.verify(gamesRepository).findPaginated(0, 10, "category", null);
    }
}