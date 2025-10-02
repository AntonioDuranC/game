package pe.com.ladc.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ladc.dto.GameRequestDTO;
import pe.com.ladc.dto.GameResponseDTO;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.enums.GameCategory;
import pe.com.ladc.service.GameService;

import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // ✅ Mockito puro
class GameResourceTest {

    @Mock
    GameService service; // ✅ mockeamos el service

    @InjectMocks
    GameResource resource; // ✅ Mockito inyecta el mock en el resource

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

    private GameResponseDTO buildResponse(Long id) {
        return GameResponseDTO.builder()
                .id(id)
                .title("FIFA 25")
                .category(GameCategory.SPORTS)
                .description("Football simulation")
                .price(BigDecimal.valueOf(59.99))
                .releaseDate(LocalDate.of(2025, 9, 30))
                .active(true)
                .availableStock(100)
                .build();
    }

    @Test
    void testCreateGame() {
        GameRequestDTO requestDTO = buildRequest();
        GameResponseDTO responseDTO = buildResponse(1L);

        when(service.createGame(any(GameRequestDTO.class))).thenReturn(responseDTO);

        Response response = resource.createGame(requestDTO);

        assertEquals(200, response.getStatus());

        @SuppressWarnings("unchecked")
        ResponseDTO<GameResponseDTO> body = (ResponseDTO<GameResponseDTO>) response.getEntity();

        assertEquals("Game created successfully", body.getMessage());
        assertEquals(201, body.getStatusCode());
        assertEquals("FIFA 25", body.getData().getTitle());

        verify(service, times(1)).createGame(requestDTO);
    }


    @Test
    void testReplaceGame() {
        GameRequestDTO requestDTO = buildRequest();
        GameResponseDTO responseDTO = buildResponse(1L);

        when(service.replaceGame(eq(1L), any(GameRequestDTO.class))).thenReturn(responseDTO);

        Response response = resource.replaceGame(1L, requestDTO);

        assertEquals(200, response.getStatus());

        @SuppressWarnings("unchecked")
        ResponseDTO<GameResponseDTO> body = (ResponseDTO<GameResponseDTO>) response.getEntity();

        assertEquals("Game replaced successfully", body.getMessage());
        assertEquals(200, body.getStatusCode());
        assertEquals(1L, body.getData().getId());

        verify(service).replaceGame(eq(1L), any(GameRequestDTO.class));
    }

    @Test
    void testUpdateGame() {
        GameRequestDTO requestDTO = buildRequest();
        GameResponseDTO responseDTO = buildResponse(1L);

        when(service.updateGame(eq(1L), any(GameRequestDTO.class))).thenReturn(responseDTO);

        Response response = resource.updateGame(1L, requestDTO);

        assertEquals(200, response.getStatus());

        @SuppressWarnings("unchecked")
        ResponseDTO<GameResponseDTO> body = (ResponseDTO<GameResponseDTO>) response.getEntity();

        assertEquals("Game updated successfully", body.getMessage());
        assertEquals(200, body.getStatusCode());
        assertEquals(1L, body.getData().getId());

        verify(service).updateGame(eq(1L), any(GameRequestDTO.class));
    }

    @Test
    void testDeleteGame() {
        doNothing().when(service).deleteGame(1L);

        Response response = resource.deleteGame(1L);

        assertEquals(200, response.getStatus());

        @SuppressWarnings("unchecked")
        ResponseDTO<Void> body = (ResponseDTO<Void>) response.getEntity();

        assertEquals("Game deleted successfully", body.getMessage());
        assertEquals(204, body.getStatusCode());

        verify(service).deleteGame(1L);
    }

    @Test
    void testGetGame() {
        GameResponseDTO responseDTO = buildResponse(1L);

        when(service.findByIdAndActive(1L)).thenReturn(responseDTO);

        Response response = resource.getGame(1L);

        assertEquals(200, response.getStatus());

        @SuppressWarnings("unchecked")
        ResponseDTO<GameResponseDTO> body = (ResponseDTO<GameResponseDTO>) response.getEntity();

        assertEquals("Game retrieved successfully", body.getMessage());
        assertEquals(200, body.getStatusCode());
        assertEquals(1L, body.getData().getId());

        verify(service).findByIdAndActive(1L);
    }

    @Test
    void testListGames() {
        when(service.findPaginated(0, 10, null)).thenReturn(List.of(buildResponse(1L)));
        when(service.count(null)).thenReturn(1L);

        Response response = resource.listGames(0, 10, null);

        assertEquals(200, response.getStatus());
        assertEquals("1", response.getHeaderString("X-Total-Count"));

        @SuppressWarnings("unchecked")
        ResponseDTO<List<GameResponseDTO>> body = (ResponseDTO<List<GameResponseDTO>>) response.getEntity();

        assertEquals("Games retrieved", body.getMessage());
        assertEquals(200, body.getStatusCode());
        assertEquals(1, body.getData().size());

        verify(service).findPaginated(0, 10, null);
        verify(service).count(null);
    }

}
