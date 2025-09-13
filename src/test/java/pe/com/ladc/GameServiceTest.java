package pe.com.ladc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ladc.dto.GameResponseDTO;
import pe.com.ladc.entity.Game;
import pe.com.ladc.enums.GameCategory;
import pe.com.ladc.exception.InvalidOperationException;
import pe.com.ladc.repository.GameRepository;
import pe.com.ladc.service.GameService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository repository;

    @InjectMocks
    private GameService service;

    private Game sampleGame;

    @BeforeEach
    void setUp() {
        sampleGame = Game.builder()
                .id(1L)
                .title("Elden Ring")
                .category(GameCategory.RPG)
                .description("Open world RPG")
                .price(new BigDecimal("69.99"))
                .releaseDate(LocalDate.of(2022, 2, 25))
                .active(true)
                .stock(null)
                .build();
    }

    @Test
    void createGame_shouldPersist_andReturnDTO_withActiveTrue() {
        // repository.persist is void on the mock; no stubbing needed
        GameResponseDTO created = service.createGame(sampleGame);

        assertNotNull(created);
        assertTrue(created.getActive());
        assertEquals(sampleGame.getTitle(), created.getTitle());
        verify(repository, times(1)).persist(sampleGame);
    }

    @Test
    void replaceGame_shouldUpdateFields_andReturnDTO() {
        Game updatedGame = Game.builder()
                .id(1L)
                .title("Elden Ring - GOTY")
                .category(GameCategory.RPG)
                .description("Updated desc")
                .price(new BigDecimal("79.99"))
                .releaseDate(LocalDate.of(2023, 1, 1))
                .build();

        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(sampleGame));

        GameResponseDTO result = service.replaceGame(updatedGame);

        assertEquals("Elden Ring - GOTY", result.getTitle());
        assertEquals("Updated desc", result.getDescription());
        assertEquals(new BigDecimal("79.99"), result.getPrice());
        verify(repository, times(1)).persist(sampleGame);
    }

    @Test
    void replaceGame_shouldThrow_whenNotFound() {
        Game updatedGame = Game.builder().id(99L).build();
        when(repository.findByIdOptional(99L)).thenReturn(Optional.empty());

        assertThrows(InvalidOperationException.class, () -> service.replaceGame(updatedGame));
        verify(repository, never()).persist(any(Game.class));
    }

    @Test
    void deleteGame_shouldSetActiveFalse_andPersist() {
        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(sampleGame));

        service.deleteGame(1L);

        assertFalse(sampleGame.getActive());
        verify(repository, times(1)).persist(sampleGame);
    }

    @Test
    void deleteGame_shouldThrow_whenNotFound() {
        when(repository.findByIdOptional(2L)).thenReturn(Optional.empty());
        assertThrows(InvalidOperationException.class, () -> service.deleteGame(2L));
    }

    @Test
    void updateGame_partialUpdate_shouldApplyChanges() {
        Game partial = Game.builder().id(1L).title("Elden Ring Patched").build();
        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(sampleGame));

        GameResponseDTO updated = service.updateGame(partial);

        assertEquals("Elden Ring Patched", updated.getTitle());
        verify(repository, never()).persist(any(Game.class)); // updateGame in our implementation does not call persist explicitly
    }

    @Test
    void findPaginated_shouldReturnListOfDTOs() {
        when(repository.findPaginated(0, 10, "category", null)).thenReturn(List.of(sampleGame));

        List<GameResponseDTO> result = service.findPaginated(0, 10, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Elden Ring", result.get(0).getTitle());
    }

    @Test
    void findById_shouldReturnDTO_whenFound() {
        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(sampleGame));

        GameResponseDTO dto = service.findById(1L);

        assertNotNull(dto);
        assertEquals("Elden Ring", dto.getTitle());
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(repository.findByIdOptional(5L)).thenReturn(Optional.empty());
        assertThrows(InvalidOperationException.class, () -> service.findById(5L));
    }
}
