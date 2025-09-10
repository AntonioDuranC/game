package pe.com.ladc.resource;

import jakarta.ws.rs.*;
import pe.com.ladc.dto.ResponseDTO;
import pe.com.ladc.entity.Games;
import pe.com.ladc.services.GameService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;


@Path("/games")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Games")
public class GameResource {

    private final GameService service;

    @Inject
    public GameResource(GameService gameService) {
        this.service = gameService;
    }

    @POST
    @Operation(
            summary = "Create game",
            description = "Create new game.")
    @RolesAllowed("admin")
    public Response create(Games game){
        Games createdGame =  service.createGame(game);
        return Response.ok(new ResponseDTO<>("Game created",200, createdGame)).build();
    }

    @PUT
    @Operation(
            summary = "Replace game",
            description = "Replace game.")
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)))
    @RolesAllowed("admin")
    public Response replace(Games game){
        Games updatedGame = service.replaceGame(game);
        return Response.ok(new ResponseDTO<>("Game updated",200, updatedGame)).build();
    }

    @PATCH
    @Operation(
            summary = " game",
            description = "Update specified game fields.")
    @APIResponse(
            responseCode = "200",
            description = "Game updated successfully.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)))
    @RolesAllowed("admin")
    public Response update(Games game) {
        Games updatedGame = service.updateGame(game);
        return Response.ok(new ResponseDTO<>("Game updated", 200, updatedGame)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Delete game",
            description = "delete game by id.")
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)))
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") int id) {
        service.deleteGame(id);
        return Response.ok(new ResponseDTO<>("Game deleted",204)).build();
    }

    @GET
    @Operation(
            summary = "Get all games",
            description = "Retrieves a paginated list of games. The results can be filtered by title."
    )
    @APIResponse(
            responseCode = "200",
            description = "Paginated list of games",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Games.class, type = SchemaType.ARRAY)))
    public Response pagedlist(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("title") String title) {

        List<Games> pagedGames = service.findPaginated(page, size, title);
        long totalGames = service.count(title);

        return Response.ok(new ResponseDTO<>("Games retrieved", 200, pagedGames))
                .header("X-Total-Count", totalGames)
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Get game by id",
            description = "Retrieves specified game by id." )
    @APIResponse(
            responseCode = "200",
            description = "Return game.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Games.class)))
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)))
    public Response get(@PathParam("id") int id){
        Games game = service.findById(id);
        return Response.ok(new ResponseDTO<>("Game retrieved", 200, game))
                .build();
    }

}

