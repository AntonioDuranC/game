package pe.com.ladc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ladc.dto.GameRequestDTO;
import pe.com.ladc.dto.GameResponseDTO;
import pe.com.ladc.entity.Game;
import pe.com.ladc.enums.GameCategory;
import pe.com.ladc.exception.InvalidOperationException;
import pe.com.ladc.repository.GameRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    GameRepository repository;

    @InjectMocks
    GameService service;

    // ---------- Helpers ----------
    private Game buildEntity(Long id) {

        return Game.builder()
                .id(id)
                .title("FIFA 25")
                .category(GameCategory.SPORTS)
                .description("Football game")
                .price(BigDecimal.valueOf(49.99))
                .releaseDate(LocalDate.of(2025, 1, 1))
                .active(true)
                .build();
    }

    private GameRequestDTO buildRequest() {
        return GameRequestDTO.builder()
                .title("FIFA 25")
                .category(GameCategory.SPORTS)
                .description("Football simulation")
                .price(BigDecimal.valueOf(59.99))
                .releaseDate(LocalDate.of(2025, 9, 30))
                .stockQuantity(100)
                .build();
    }

    @Test
    void testFindById_success() {
        Game entity = buildEntity(1L);
        when(repository.findByIdAndActive(1L)).thenReturn(Optional.of(entity));

        GameResponseDTO responseDTO = service.findByIdAndActive(1L);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals(50, responseDTO.getAvailableStock());
    }

    @Test
    void testFindById_notFound() {
        when(repository.findByIdAndActive(1L)).thenReturn(Optional.empty());
        assertThrows(InvalidOperationException.class, () -> service.findByIdAndActive(1L));
    }

    @Test
    void testDeleteGame_success() {
        Game entity = buildEntity(1L);
        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(entity));

        service.deleteGame(1L);

        assertFalse(entity.getActive());
        verify(repository).persist(entity);
    }

    @Test
    void testCreateGame_success() {
        GameRequestDTO requestDTO = buildRequest();

        doAnswer(invocation -> {
            Game g = invocation.getArgument(0);
            g.setId(1L);
            return null;
        }).when(repository).persist(any(Game.class));

        GameResponseDTO created = service.createGame(requestDTO);

        assertNotNull(created);
        assertEquals("FIFA 25", created.getTitle());
        assertTrue(created.getActive());
        assertEquals(100, created.getAvailableStock());
    }

    @Test
    void testDeleteGame_notFound() {
        when(repository.findByIdOptional(99L)).thenReturn(Optional.empty());
        assertThrows(InvalidOperationException.class, () -> service.deleteGame(99L));
    }

    @Test
    void testReplaceGame_success() {
        Game existing = buildEntity(1L);
        GameRequestDTO requestDTO = buildRequest();

        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(existing));

        GameResponseDTO updated = service.replaceGame(1L, requestDTO);

        assertEquals("FIFA 25", updated.getTitle());
        verify(repository).persist(existing);
    }

    @Test
    void testUpdateGame_success() {
        Game existing = buildEntity(1L);
        GameRequestDTO requestDTO = buildRequest();
        requestDTO.setDescription("Updated description");

        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(existing));

        GameResponseDTO updated = service.updateGame(1L, requestDTO);

        assertEquals("Updated description", updated.getDescription());
        verify(repository).persist(existing);
    }

    @Test
    void testFindPaginated_success() {
        when(repository.findPaginated(0, 10, "category", null)).thenReturn(
                List.of(buildEntity(1L)));
        List<GameResponseDTO> result = service.findPaginated(0, 10, null);

        assertEquals(1, result.size());
        assertEquals(40, result.get(0).getAvailableStock());
    }

    @Test
    void testCount_withFilter() {
        when(repository.countByTitle("FIFA")).thenReturn(5L);
        long count = service.count("FIFA");
        assertEquals(5L, count);
    }

    @Test
    void testChangePrice_invalidValue() {
        Game entity = buildEntity(1L);
        assertThrows(IllegalArgumentException.class, () -> entity.changePrice(BigDecimal.ZERO));
    }

}
