package pe.com.ladc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.com.ladc.dto.GameRequestDTO;
import pe.com.ladc.dto.GameResponseDTO;
import pe.com.ladc.entity.Game;
import pe.com.ladc.entity.Stock;
import pe.com.ladc.repository.GameRepository;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceParameterizedTest {

    private static final Path DATA_DIR = Paths.get("src/test/resources/test-data/games");
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Mock
    private GameRepository repository;

    @InjectMocks
    private GameService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- Proveedor de JSON ----------
    static Stream<Path> gameJsonProvider() throws IOException {
        return Files.list(DATA_DIR).filter(p -> p.toString().endsWith(".json"));
    }

    @ParameterizedTest(name = "Procesando archivo: {0}")
    @MethodSource("gameJsonProvider")
    void testCreateGameFromJson(Path jsonFile) throws Exception {
        JsonNode jsonNode = mapper.readTree(jsonFile.toFile());
        GameRequestDTO request = mapper.treeToValue(jsonNode, GameRequestDTO.class);

        // Caso: inválido (si falta campo o valores no válidos)

        final boolean isInvalid =
            request.getTitle() == null || request.getTitle().isBlank() ||
            request.getPrice() == null || request.getPrice().doubleValue() <= 0 ||
            request.getStockQuantity() < 0;

        if (isInvalid) {
            // 🔹 Los juegos inválidos deben lanzar excepción
            assertThrows(RuntimeException.class, () -> service.createGame(request));
        } else {
            // 🔹 Simulamos persistencia
            doAnswer(invocation -> {
                Game game = invocation.getArgument(0);
                game.setId(1L);
                game.setStock(Stock.builder()
                        .totalStock(request.getStockQuantity())
                        .reservedStock(0)
                        .game(game)
                        .build());
                return null;
            }).when(repository).persist(any(Game.class));

            GameResponseDTO response = service.createGame(request);

            // Validaciones
            assertNotNull(response);
            assertEquals(request.getTitle(), response.getTitle());
            assertEquals(request.getCategory(), response.getCategory());
            assertEquals(request.getDescription(), response.getDescription());
            assertEquals(request.getPrice(), response.getPrice());
            assertEquals(request.getReleaseDate(), response.getReleaseDate());
            assertEquals(request.getStockQuantity(), response.getAvailableStock());
            assertTrue(response.getActive());

            verify(repository).persist(any(Game.class));
        }
    }
}
