package pe.com.ladc.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pe.com.ladc.dto.GameRequestDTO;
import pe.com.ladc.dto.GameResponseDTO;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.service.GameService;

import java.util.List;

@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Games", description = "Game management endpoints")
public class GameResource {

    private final GameService service;

    @Inject
    public GameResource(GameService service) {
        this.service = service;
    }

    @POST
    @Operation(summary = "Create a new game")
    public Response createGame(GameRequestDTO request) {
        GameResponseDTO created = service.createGame(request);
        return Response.ok(new ResponseDTO<>("Game created successfully", 201, created)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Replace an existing game (full update)")
    public Response replaceGame(@PathParam("id") Long id, GameRequestDTO request) {
        GameResponseDTO updated = service.replaceGame(id, request);
        return Response.ok(new ResponseDTO<>("Game replaced successfully", 200, updated)).build();
    }

    @PATCH
    @Path("/{id}")
    @Operation(summary = "Partially update an existing game")
    public Response updateGame(@PathParam("id") Long id, GameRequestDTO request) {
        GameResponseDTO updated = service.updateGame(id, request);
        return Response.ok(new ResponseDTO<>("Game updated successfully", 200, updated)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete (deactivate) a game")
    public Response deleteGame(@PathParam("id") Long id) {
        service.deleteGame(id);
        return Response.ok(new ResponseDTO<>("Game deleted successfully", 204)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a game by ID")
    public Response getGame(@PathParam("id") Long id) {
        GameResponseDTO game = service.findByIdAndActive(id);
        return Response.ok(new ResponseDTO<>("Game retrieved successfully", 200, game)).build();
    }

    @GET
    @Operation(summary = "List games with pagination and optional title filter")
    public Response listGames(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("title") String title) {

        List<GameResponseDTO> pagedGames = service.findPaginated(page, size, title);
        long totalGames = service.count(title);

        return Response.ok(new ResponseDTO<>("Games retrieved", 200, pagedGames))
                .header("X-Total-Count", totalGames)
                .build();
    }

}
